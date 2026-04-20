package com.github.salilvnair.convengdemo.llm.runtime;

import com.github.salilvnair.convengdemo.config.BuilderStudioLlmRuntimeProperties;
import com.github.salilvnair.convengine.builder.api.service.BuilderStudioLlmRuntimeClientFactory;
import com.github.salilvnair.convengine.builder.api.service.BuilderStudioLlmRuntimeService;
import com.github.salilvnair.convengine.engine.session.EngineSession;
import com.github.salilvnair.convengine.llm.core.LlmClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BuilderStudioLlmRuntimeServiceImpl implements BuilderStudioLlmRuntimeService {

    private final BuilderStudioLlmRuntimeProperties props;
    private final List<BuilderStudioLlmRuntimeClientFactory> clientFactories;
    private final AtomicReference<String> activeProvider = new AtomicReference<>();
    private final AtomicReference<Double> activeTemperature = new AtomicReference<>();
    private final Map<String, String> activeModels = new ConcurrentHashMap<>();
    private final Map<String, BuilderStudioLlmRuntimeClientFactory> factoriesByProvider = new HashMap<>();

    @jakarta.annotation.PostConstruct
    void init() {
        factoriesByProvider.putAll(clientFactories.stream()
                .collect(Collectors.toMap(BuilderStudioLlmRuntimeClientFactory::providerKey, f -> f, (a, b) -> a)));

        activeProvider.set(hasText(props.getProvider()) ? props.getProvider() : "openai");
        activeTemperature.set(props.getTemperature() != null ? props.getTemperature() : 0.3);
        if (hasText(props.getOpenai().getModel())) activeModels.put("openai", props.getOpenai().getModel());
        if (hasText(props.getLmstudio().getModel())) activeModels.put("lmstudio", props.getLmstudio().getModel());
    }

    @Override
    public Map<String, Object> availableProviders() {
        Map<String, Object> out = new LinkedHashMap<>();
        out.put("provider", activeProvider.get());
        out.put("temperature", activeTemperature.get());
        out.put("defaults", Map.of(
                "provider", defaultProvider(),
                "temperature", defaultTemperature(),
                "model", defaultModel(defaultProvider())
        ));
        appendProvider(out, "openai", "OpenAI", props.getOpenai());
        appendProvider(out, "lmstudio", "LM Studio", props.getLmstudio());
        return out;
    }

    @Override
    public Map<String, Object> changeProvider(String provider, String model, Double temperature) {
        String resolvedProvider = resolveProvider(provider, model);
        ensureSupportedProvider(resolvedProvider);
        activeProvider.set(resolvedProvider);
        if (hasText(model)) activeModels.put(resolvedProvider, model);
        if (temperature != null) activeTemperature.set(temperature);
        return availableProviders();
    }

    @Override
    public String generateText(String provider, String model, Double temperature,
                               EngineSession session, String hint, String contextJson) {
        Selection selection = resolveSelection(provider, model, temperature);
        return clientFor(selection).generateText(session, hint, contextJson);
    }

    @Override
    public String generateJson(String provider, String model, Double temperature,
                               EngineSession session, String hint, String jsonSchema, String contextJson) {
        Selection selection = resolveSelection(provider, model, temperature);
        return clientFor(selection).generateJson(session, hint, jsonSchema, contextJson);
    }

    @Override
    public String generateJsonStrict(String provider, String model, Double temperature,
                                     EngineSession session, String hint, String jsonSchema, String contextJson) {
        Selection selection = resolveSelection(provider, model, temperature);
        return clientFor(selection).generateJsonStrict(session, hint, jsonSchema, contextJson);
    }

    private void appendProvider(Map<String, Object> out, String key, String name,
                                BuilderStudioLlmRuntimeProperties.ProviderConfig cfg) {
        if (!hasText(cfg.getModel()) && !hasText(cfg.getBaseUrl())) return;
        Map<String, Object> provider = new LinkedHashMap<>();
        provider.put("name", name);
        provider.put("provider", key);
        provider.put("model", activeModels.getOrDefault(key, cfg.getModel()));
        provider.put("configured-model", cfg.getModel());
        provider.put("base-url", cfg.getBaseUrl());
        provider.put("api-key-configured", hasText(cfg.getApiKey()));
        provider.put("models", availableModels(cfg));
        out.put(key, provider);
    }

    private LlmClient clientFor(Selection selection) {
        BuilderStudioLlmRuntimeClientFactory factory = factoriesByProvider.get(selection.provider());
        if (factory == null) {
            throw new IllegalStateException("No runtime LLM client factory for provider: " + selection.provider());
        }
        Map<String, Object> runtimeConfig = new HashMap<>();
        runtimeConfig.put("model", selection.model());
        runtimeConfig.put("temperature", selection.temperature());
        runtimeConfig.put("apiKey", selection.config().getApiKey());
        runtimeConfig.put("baseUrl", selection.config().getBaseUrl());
        return factory.create(runtimeConfig);
    }

    private Selection resolveSelection(String provider, String model, Double temperature) {
        String resolvedProvider = resolveProvider(provider, model);
        ensureSupportedProvider(resolvedProvider);
        BuilderStudioLlmRuntimeProperties.ProviderConfig cfg = providerConfig(resolvedProvider);
        String resolvedModel = hasText(model) ? model : activeModels.getOrDefault(resolvedProvider, cfg.getModel());
        double resolvedTemperature = temperature != null ? temperature : (activeTemperature.get() != null ? activeTemperature.get() : defaultTemperature());
        return new Selection(resolvedProvider, resolvedModel, resolvedTemperature, cfg);
    }

    private String resolveProvider(String provider, String model) {
        if (hasText(provider)) return provider;
        if (hasText(model)) {
            for (String candidate : List.of("openai", "lmstudio")) {
                BuilderStudioLlmRuntimeProperties.ProviderConfig cfg = providerConfig(candidate);
                if (availableModels(cfg).contains(model)) return candidate;
            }
        }
        return activeProvider.get();
    }

    private BuilderStudioLlmRuntimeProperties.ProviderConfig providerConfig(String provider) {
        return switch (provider) {
            case "openai" -> props.getOpenai();
            case "lmstudio" -> props.getLmstudio();
            default -> throw new IllegalArgumentException("Unsupported provider: " + provider);
        };
    }

    private List<String> availableModels(BuilderStudioLlmRuntimeProperties.ProviderConfig cfg) {
        List<String> models = new ArrayList<>();
        if (hasText(cfg.getModel())) models.add(cfg.getModel());
        for (String model : cfg.getModels()) {
            if (hasText(model) && !models.contains(model)) models.add(model);
        }
        return models;
    }

    private String defaultProvider() {
        return hasText(props.getProvider()) ? props.getProvider() : "openai";
    }

    private double defaultTemperature() {
        return props.getTemperature() != null ? props.getTemperature() : 0.3;
    }

    private String defaultModel(String provider) {
        BuilderStudioLlmRuntimeProperties.ProviderConfig cfg = providerConfig(provider);
        return cfg.getModel();
    }

    private void ensureSupportedProvider(String provider) {
        if (!factoriesByProvider.containsKey(provider)) {
            throw new IllegalArgumentException("Unsupported provider: " + provider);
        }
    }

    private boolean hasText(String value) {
        return value != null && !value.isBlank();
    }

    private record Selection(String provider, String model, double temperature,
                             BuilderStudioLlmRuntimeProperties.ProviderConfig config) {
    }
}