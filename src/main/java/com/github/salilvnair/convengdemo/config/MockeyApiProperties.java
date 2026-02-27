package com.github.salilvnair.convengdemo.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "convengine.demo.mockey")
@Getter
@Setter
public class MockeyApiProperties {
    private String baseUrl = "http://localhost:31333";
    private String apiKey = "demo-api-key";
}
