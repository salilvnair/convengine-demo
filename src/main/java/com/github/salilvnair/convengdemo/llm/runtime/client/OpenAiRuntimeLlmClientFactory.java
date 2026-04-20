package com.github.salilvnair.convengdemo.llm.runtime.client;

import com.github.salilvnair.api.processor.rest.facade.RestWebServiceFacade;
import com.github.salilvnair.convengine.builder.api.service.BuilderStudioLlmRuntimeClientFactory;
import com.github.salilvnair.convengine.llm.core.LlmClient;
import com.github.salilvnair.convengdemo.llm.provider.openai.OpenAiLlmClient;
import com.github.salilvnair.convengdemo.llm.provider.openai.handler.OpenAiRestWebserviceHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class OpenAiRuntimeLlmClientFactory implements BuilderStudioLlmRuntimeClientFactory {

    private final RestWebServiceFacade restWebServiceFacade;
    private final OpenAiRestWebserviceHandler handler;

    @Override
    public String providerKey() {
        return "openai";
    }

    @Override
    public LlmClient create(Map<String, Object> runtimeConfig) {
        String model = (String) runtimeConfig.get("model");
        double temperature = ((Number) runtimeConfig.get("temperature")).doubleValue();

        return new OpenAiLlmClient(restWebServiceFacade, handler, model, temperature, runtimeConfig);
    }
}
