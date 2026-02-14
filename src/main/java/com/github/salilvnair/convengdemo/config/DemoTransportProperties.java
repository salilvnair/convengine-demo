package com.github.salilvnair.convengdemo.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "convengine.demo")
@Getter
@Setter
public class DemoTransportProperties {

    private StreamMode streamMode = StreamMode.SSE;
    private StepHook stepHook = new StepHook();

    public enum StreamMode {
        SSE,
        STOMP,
        BOTH
    }

    @Getter
    @Setter
    public static class StepHook {
        private boolean enabled = true;
    }
}
