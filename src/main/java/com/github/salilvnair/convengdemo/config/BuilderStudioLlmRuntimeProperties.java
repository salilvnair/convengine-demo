package com.github.salilvnair.convengdemo.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "convengine.llm")
public class BuilderStudioLlmRuntimeProperties {

    private String provider;
    private Double temperature = 0.3;
    private ProviderConfig openai = new ProviderConfig();
    private ProviderConfig lmstudio = new ProviderConfig();

    @Getter
    @Setter
    public static class ProviderConfig {
        private String apiKey;
        private String model;
        private String baseUrl;
        private List<String> models = new ArrayList<>();
    }
}