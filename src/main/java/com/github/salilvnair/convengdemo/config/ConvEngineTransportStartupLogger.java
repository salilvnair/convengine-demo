package com.github.salilvnair.convengdemo.config;

import com.github.salilvnair.convengine.config.ConvEngineTransportConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ConvEngineTransportStartupLogger {

    private final DemoTransportProperties demoTransportProperties;
    private final ConvEngineTransportConfig transportConfig;
    private final Environment environment;

    @EventListener(ApplicationReadyEvent.class)
    public void logTransportConfiguration() {
        int port = environment.getProperty("local.server.port", Integer.class,
                environment.getProperty("server.port", Integer.class, 8080));

        boolean sseEnabled = transportConfig.getSse().isEnabled();
        boolean stompEnabled = transportConfig.getStomp().isEnabled();
        DemoTransportProperties.StreamMode mode = demoTransportProperties.getStreamMode();

        validateMode(mode, sseEnabled, stompEnabled);

        log.info("ConvEngine demo stream mode: {}", mode);
        log.info("SSE enabled: {}", sseEnabled);
        log.info("STOMP enabled: {}", stompEnabled);

        if (sseEnabled) {
            log.info("SSE endpoint: http://localhost:{}/api/v1/conversation/stream/{{conversationId}}", port);
        }
        if (stompEnabled) {
            log.info("WebSocket endpoint: ws://localhost:{}{}", port, transportConfig.getStomp().getEndpoint());
            log.info("STOMP topic: {}/{{conversationId}}", transportConfig.getStomp().getAuditDestinationBase());
        }

        // Explicit console print requested for quick runtime visibility.
        System.out.println("[ConvEngineDemo] transport mode=" + mode
                + ", sse=" + sseEnabled
                + ", stomp=" + stompEnabled);
    }

    private void validateMode(
            DemoTransportProperties.StreamMode mode,
            boolean sseEnabled,
            boolean stompEnabled
    ) {
        boolean valid = switch (mode) {
            case SSE -> sseEnabled && !stompEnabled;
            case STOMP -> !sseEnabled && stompEnabled;
            case BOTH -> sseEnabled && stompEnabled;
        };

        if (!valid) {
            throw new IllegalStateException(
                    "Invalid transport config for mode=" + mode
                            + ". Expected: SSE=(true,false), STOMP=(false,true), BOTH=(true,true)."
            );
        }
    }
}
