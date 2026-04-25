package com.github.salilvnair.convengdemo.llm.provider.openai.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.salilvnair.api.processor.rest.handler.RestWebServiceDelegate;
import com.github.salilvnair.api.processor.rest.handler.RestWebServiceHandler;
import com.github.salilvnair.api.processor.rest.model.RestWebServiceRequest;
import com.github.salilvnair.api.processor.rest.model.RestWebServiceResponse;
import com.github.salilvnair.convengine.engine.history.model.ConversationTurn;
import com.github.salilvnair.convengine.engine.session.EngineSession;
import com.github.salilvnair.convengine.llm.base.type.OutputType;
import com.github.salilvnair.convengdemo.llm.provider.openai.context.OpenAiApiContext;
import com.github.salilvnair.convengdemo.llm.provider.openai.delegate.OpenAiRestWebserviceDelegate;
import com.github.salilvnair.convengdemo.llm.provider.openai.model.OpenAiRequest;
import com.github.salilvnair.convengdemo.llm.provider.openai.model.OpenAiResponse;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Component
@AllArgsConstructor
public class OpenAiRestWebserviceHandler implements RestWebServiceHandler {

    private final OpenAiRestWebserviceDelegate delegate;
    private final ObjectMapper mapper;

    @Override
    public RestWebServiceDelegate delegate() {
        return delegate;
    }

    @SneakyThrows
    @Override
    public RestWebServiceRequest prepareRequest(
            Map<String, Object> restWsMap,
            Object... objects
    ) {
        OpenAiApiContext ctx = (OpenAiApiContext) objects[0];

        OpenAiRequest req = new OpenAiRequest();
        req.setModel(ctx.getModel());
        req.setTemperature(ctx.getTemperature());

        // ----------------------------
        // TEXT MODE (always non-strict)
        // ----------------------------
        if (OutputType.TEXT.equals(ctx.getType())) {

            List<OpenAiRequest.Message> messages =
                    buildTextMessages(ctx.getHint(), ctx.getUserContext(), ctx.getSession());

            req.setMessages(messages);
            ctx.setMessages(messages);
            return req;
        }

        // ----------------------------
        // JSON MODE
        // ----------------------------
        if (OutputType.JSON.equals(ctx.getType())) {

            List<OpenAiRequest.Message> messages = buildJsonMessages(ctx);

            // 🔒 STRICT JSON → Responses API
            if (ctx.isStrictJson()) {
                req.setInput(messages);

                OpenAiRequest.Format format = new OpenAiRequest.Format();
                format.setName("output");
                format.setType("json_schema");
                format.setSchema(mapper.readValue(ctx.getJsonSchema(), Object.class));
                format.setStrict(true);

                OpenAiRequest.Text text = new OpenAiRequest.Text();
                text.setFormat(format);

                req.setText(text);
                ctx.setMessages(messages);
                return req;
            }

            // 🟡 NON-STRICT JSON (Chat-style, backward compatible)
            req.setMessages(messages);
            ctx.setMessages(messages);
            return req;
        }

        throw new IllegalStateException("Unsupported OutputType: " + ctx.getType());
    }

    @Override
    public void processResponse(
            RestWebServiceRequest request,
            RestWebServiceResponse response,
            Map<String, Object> restWsMap,
            Object... objects
    ) {
        OpenAiApiContext ctx = (OpenAiApiContext) objects[0];
        ctx.setResponse((OpenAiResponse) response);
    }

    // ------------------------------------------------------------------
    // Message builders
    // ------------------------------------------------------------------

    private List<OpenAiRequest.Message> buildTextMessages(
            String hint,
            String userText,
            EngineSession session
    ) {
        List<ConversationTurn> history = session != null ? session.conversionHistory() : Collections.emptyList();
        List<OpenAiRequest.Message> msgs = new ArrayList<>();
        msgs.add(OpenAiRequest.Message.builder()
                .role("system")
                .content("You are a concise conversational assistant.")
                .build());
        msgs.add(OpenAiRequest.Message.builder()
                .role("system")
                .content(hint)
                .build());
        // Inject prior conversation turns before the current user message
        for (ConversationTurn turn : history) {
            msgs.add(OpenAiRequest.Message.builder().role("user").content(turn.user()).build());
            msgs.add(OpenAiRequest.Message.builder().role("assistant").content(turn.assistant()).build());
        }
        msgs.add(OpenAiRequest.Message.builder()
                .role("user")
                .content(userText)
                .build());
        return msgs;
    }

    private List<OpenAiRequest.Message> buildJsonMessages( OpenAiApiContext ctx ) {
        String hint = ctx.getHint();
        String jsonSchema = ctx.getJsonSchema();
        String userContext = ctx.getUserContext();
        var msgs = new java.util.ArrayList<>(List.of(
                OpenAiRequest.Message.builder()
                        .role("system")
                        .content("""
                                You are a JSON extraction engine.
                                You MUST return ONLY valid JSON.
                                Do NOT add explanations.
                                Do NOT use markdown.
                                """)
                        .build(),
                OpenAiRequest.Message.builder()
                        .role("system")
                        .content("JSON Schema:\n" + jsonSchema)
                        .build(),
                OpenAiRequest.Message.builder()
                        .role("system")
                        .content(hint)
                        .build()));
        // Include user context (input) as a user message so the LLM sees the
        // actual data to extract from — mirrors text-mode behaviour.
        if (userContext != null && !userContext.isBlank()) {
            msgs.add(OpenAiRequest.Message.builder()
                    .role("user")
                    .content(userContext)
                    .build());
        }
        return msgs;
    }

    @Override
    public String webServiceName() {
        return "OpenAiChatCompletion";
    }
}
