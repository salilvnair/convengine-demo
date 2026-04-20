package com.github.salilvnair.convengdemo.llm.provider.lmstudio;

import com.github.salilvnair.api.processor.rest.facade.RestWebServiceFacade;
import com.github.salilvnair.convengdemo.llm.provider.lmstudio.context.LmStudioEmbeddingApiContext;
import com.github.salilvnair.convengdemo.llm.provider.lmstudio.handler.LmStudioEmbeddingRestWebserviceHandler;
import com.github.salilvnair.convengdemo.llm.provider.openai.model.OpenAiRequest;
import com.github.salilvnair.convengdemo.llm.provider.openai.model.OpenAiResponse;
import com.github.salilvnair.convengine.entity.CeLlmCallLog;
import com.github.salilvnair.convengine.engine.session.EngineSession;
import com.github.salilvnair.convengine.llm.base.type.OutputType;
import com.github.salilvnair.convengine.llm.context.LlmInvocationContext;
import com.github.salilvnair.convengine.llm.core.LlmClient;
import com.github.salilvnair.convengine.repo.LlmCallLogRepository;
import com.github.salilvnair.convengdemo.llm.provider.lmstudio.context.LmStudioApiContext;
import com.github.salilvnair.convengdemo.llm.provider.lmstudio.handler.LmStudioRestWebserviceHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Component
@ConditionalOnProperty(
        name = "convengine.llm.provider",
        havingValue = "lmstudio"
)
public class LmStudioLlmClient implements LlmClient {

    private final LlmCallLogRepository logRepo;
    private final RestWebServiceFacade restWebServiceFacade;
    private final LmStudioRestWebserviceHandler handler;
    private final LmStudioEmbeddingRestWebserviceHandler embeddingHandler;

    @Value("${convengine.llm.lmstudio.model}")
    private String model;

    private Map<String, Object> runtimeOverrides;

    /** Runtime constructor — no Spring injection, no logging. */
    public LmStudioLlmClient(RestWebServiceFacade restWebServiceFacade,
                             LmStudioRestWebserviceHandler handler,
                             String model,
                             Map<String, Object> runtimeOverrides) {
        this.restWebServiceFacade = restWebServiceFacade;
        this.handler = handler;
        this.model = model;
        this.logRepo = null;
        this.embeddingHandler = null;
        this.runtimeOverrides = runtimeOverrides;
    }

    @Override
    public String generateText(EngineSession session, String hint, String context) {
        return call(
                LmStudioApiContext.builder()
                        .session(session)
                        .model(model)
                        .hint(hint)
                        .userContext(context)
                        .type(OutputType.TEXT)
                        .build()
        );
    }

    @Override
    public String generateJson(EngineSession session, String hint, String jsonSchema, String context) {
        return call(
                LmStudioApiContext.builder()
                        .session(session)
                        .model(model)
                        .hint(hint)
                        .jsonSchema(jsonSchema)
                        .userContext(context)
                        .type(OutputType.JSON)
                        .build()
        );
    }

    @Override
    public String generateJsonStrict(EngineSession session, String hint, String jsonSchema, String context) {
        return call(
                LmStudioApiContext.builder()
                        .session(session)
                        .model(model)
                        .hint(hint)
                        .jsonSchema(jsonSchema)
                        .userContext(context)
                        .type(OutputType.JSON)
                        .strictJson(true)
                        .build()
        );
    }

    @Override
    public float[] generateEmbedding(EngineSession session, String input) {
        LmStudioEmbeddingApiContext ctx = LmStudioEmbeddingApiContext
                                            .builder()
                                            .session(session)
                                            .model("text-embedding-multilingual-e5-large-instruct")
                                            .input(input)
                                            .build();

        restWebServiceFacade.initiate(embeddingHandler, new HashMap<>(), ctx);

        return ctx.getEmbedding();
    }

    private String call(LmStudioApiContext apiContext) {
        if (logRepo == null) {
            return callNoLog(apiContext);
        }
        LlmInvocationContext ctx = LlmInvocationContext.get();

        CeLlmCallLog log = CeLlmCallLog.builder()
                .conversationId(ctx != null ? ctx.conversationId() : null)
                .intentCode(ctx != null ? ctx.intent() : null)
                .stateCode(ctx != null ? ctx.state() : null)
                .provider("lmstudio")
                .model(model)
                .userContext(apiContext.getUserContext())
                .createdAt(OffsetDateTime.now())
                .success(false)
                .build();



        try {
            restWebServiceFacade.initiate(handler, new HashMap<>(), apiContext);
            String prompt = flattenPrompt(apiContext.getMessages());
            log.setPromptText(prompt == null ? "" : prompt);
            OpenAiResponse response = apiContext.getResponse();
            String content = response.extractText();
            content = content.replaceAll("(?s)<think>.*?</think>", "").trim();
            content = content.replace("```json\n", "").trim();
            content = content.replace("\n```", "").trim();
            log.setResponseText(content);
            log.setSuccess(true);
            return content;
        } catch (Exception e) {
            log.setErrorMessage(e.getMessage());
            throw e;
        } finally {
            logRepo.save(log);
        }
    }

    private String callNoLog(LmStudioApiContext apiContext) {
        restWebServiceFacade.initiate(handler, runtimeOverrides != null ? new HashMap<>(runtimeOverrides) : new HashMap<>(), apiContext);
        OpenAiResponse response = apiContext.getResponse();
        String content = response != null ? response.extractText() : "";
        if (content == null) return "";
        content = content.replaceAll("(?s)<think>.*?</think>", "").trim();
        content = content.replace("```json\n", "").trim();
        content = content.replace("\n```", "").trim();
        return content;
    }

    private String flattenPrompt(List<OpenAiRequest.Message> messages) {
        return messages.stream()
                .map(m -> "[" + m.getRole() + "] " + m.getContent())
                .collect(Collectors.joining("\n\n"));
    }
}
