package com.github.salilvnair.convengdemo.llm.provider.openai.delegate;

import com.github.salilvnair.api.processor.rest.handler.RestWebServiceDelegate;
import com.github.salilvnair.api.processor.rest.model.RestWebServiceRequest;
import com.github.salilvnair.api.processor.rest.model.RestWebServiceResponse;
import com.github.salilvnair.convengine.engine.session.EngineSession;
import com.github.salilvnair.convengine.transport.verbose.ConvEngineVerboseAdapter;
import com.github.salilvnair.convengdemo.llm.provider.openai.context.OpenAiApiContext;
import com.github.salilvnair.convengdemo.llm.provider.openai.model.OpenAiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class OpenAiRestWebserviceDelegate implements RestWebServiceDelegate {
    private static final String SOURCE = "OpenAiRestWebserviceDelegate";
    private static final String DETERMINANT_AGENT_RETRYING = "AGENT_RETRYING";
    private static final String DETERMINANT_AGENT_RETRY_FAILED = "AGENT_RETRY_FAILED";
    private static final String DETERMINANT_AGENT_RETRY_EXHAUSTED = "AGENT_RETRY_EXHAUSTED";
    private static final ThreadLocal<OpenAiApiContext> ACTIVE_CONTEXT = new ThreadLocal<>();

    private final ObjectProvider<ConvEngineVerboseAdapter> verboseAdapterProvider;

    @Value("${convengine.llm.openai.api-key}")
    private String apiKey;
    @Value("${convengine.llm.openai.base-url}")
    private String baseUrl;

    @Override
    public RestWebServiceResponse invoke(RestWebServiceRequest restWebServiceRequest, Map<String, Object> map,
            Object... objects) {
        OpenAiApiContext ctx = (OpenAiApiContext) objects[0];
        ACTIVE_CONTEXT.set(ctx);
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + apiKey);
        headers.set("Content-Type", "application/json");
        HttpEntity<?> requestEntity = new HttpEntity<>(restWebServiceRequest, headers);
        RestTemplate restTemplate = new RestTemplate();
        String apiUrl = baseUrl + (ctx.isStrictJson() ? "/v1/responses" : "/v1/chat/completions");
        try {
            ResponseEntity<OpenAiResponse> responseEntity = restTemplate.exchange(apiUrl, HttpMethod.POST, requestEntity,
                    OpenAiResponse.class);
            ACTIVE_CONTEXT.remove();
            return responseEntity.getBody();
        } catch (Exception ex) {
            if (!matchesRetryWhitelist(ex)) {
                ACTIVE_CONTEXT.remove();
            }
            throw ex;
        }
    }

    @Override
    public boolean retry() {
        return true;
    }

    @Override
    public int maxRetries() {
        return 3;
    }

    @Override
    public int delay() {
        return 30;
    }

    @Override
    public TimeUnit delayTimeUnit() {
        return TimeUnit.SECONDS;
    }

    @Override
    public List<String> whiteListedExceptions() {
        return List.of("429", "Too Many Requests", "Rate limit reached");
    }

    @Override
    public void onRetryScheduled(int nextAttempt, int maxRetries, long delayMs, Exception lastError) {
        EngineSession session = activeSession();
        ConvEngineVerboseAdapter adapter = verboseAdapterProvider.getIfAvailable();
        if (session == null || adapter == null) {
            return;
        }
        Map<String, Object> metadata = new LinkedHashMap<>();
        metadata.put("attempt", nextAttempt);
        metadata.put("maxRetries", maxRetries);
        metadata.put("delayMs", delayMs);
        metadata.put("delaySeconds", TimeUnit.MILLISECONDS.toSeconds(delayMs));
        metadata.put("error", lastError == null ? null : lastError.getMessage());
        String text = "Agent is retrying... attempt %d/%d in %d seconds."
                .formatted(nextAttempt, maxRetries, TimeUnit.MILLISECONDS.toSeconds(delayMs));
        adapter.publishText(session, SOURCE, DETERMINANT_AGENT_RETRYING, text, false, metadata);
    }

    @Override
    public void onRetryAttemptFailed(int attempt, int maxRetries, Exception error) {
        EngineSession session = activeSession();
        ConvEngineVerboseAdapter adapter = verboseAdapterProvider.getIfAvailable();
        if (session == null || adapter == null) {
            return;
        }
        Map<String, Object> metadata = new LinkedHashMap<>();
        metadata.put("attempt", attempt);
        metadata.put("maxRetries", maxRetries);
        metadata.put("error", error == null ? null : error.getMessage());
        String text = "Retry attempt %d/%d failed."
                .formatted(attempt, maxRetries);
        adapter.publishText(session, SOURCE, DETERMINANT_AGENT_RETRY_FAILED, text, true, metadata);
        if (attempt >= maxRetries) {
            ACTIVE_CONTEXT.remove();
        }
    }

    @Override
    public void onMaxRetriesExceeded(int maxRetries, Exception lastError) {
        EngineSession session = activeSession();
        ConvEngineVerboseAdapter adapter = verboseAdapterProvider.getIfAvailable();
        if (session != null && adapter != null) {
            Map<String, Object> metadata = new LinkedHashMap<>();
            metadata.put("maxRetries", maxRetries);
            metadata.put("error", lastError == null ? null : lastError.getMessage());
            String text = "Retry limit reached after %d attempts."
                    .formatted(maxRetries);
            adapter.publishText(session, SOURCE, DETERMINANT_AGENT_RETRY_EXHAUSTED, text, true, metadata);
        }
        ACTIVE_CONTEXT.remove();
    }

    private EngineSession activeSession() {
        OpenAiApiContext ctx = ACTIVE_CONTEXT.get();
        return ctx == null ? null : ctx.getSession();
    }

    private boolean matchesRetryWhitelist(Exception error) {
        if (error == null || error.getMessage() == null) {
            return false;
        }
        String message = error.getMessage().toLowerCase(Locale.ROOT);
        for (String marker : whiteListedExceptions()) {
            if (marker == null || marker.isBlank()) {
                continue;
            }
            if (message.contains(marker.toLowerCase(Locale.ROOT))) {
                return true;
            }
        }
        return false;
    }
}
