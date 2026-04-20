package com.github.salilvnair.convengdemo.llm.runtime.client;

import com.github.salilvnair.api.processor.rest.facade.RestWebServiceFacade;
import com.github.salilvnair.convengine.builder.api.service.BuilderStudioLlmRuntimeClientFactory;
import com.github.salilvnair.convengine.llm.core.LlmClient;
import com.github.salilvnair.convengdemo.llm.provider.lmstudio.LmStudioLlmClient;
import com.github.salilvnair.convengdemo.llm.provider.lmstudio.handler.LmStudioRestWebserviceHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class LmStudioRuntimeLlmClientFactory implements BuilderStudioLlmRuntimeClientFactory {

    private final RestWebServiceFacade restWebServiceFacade;
    private final LmStudioRestWebserviceHandler handler;

    @Override
    public String providerKey() {
        return "lmstudio";
    }

    @Override
    public LlmClient create(Map<String, Object> runtimeConfig) {
        String model = (String) runtimeConfig.get("model");

        return new LmStudioLlmClient(restWebServiceFacade, handler, model, runtimeConfig);
    }
}
