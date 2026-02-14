package com.github.salilvnair.convengdemo.config;

import com.github.salilvnair.convengine.engine.hook.EngineStepHook;
import com.github.salilvnair.convengine.engine.pipeline.StepResult;
import com.github.salilvnair.convengine.engine.session.EngineSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Set;

@Slf4j
@Component
@RequiredArgsConstructor
public class DemoEngineStepHook implements EngineStepHook {

    private static final Set<String> TRACKED_STEPS = Set.of(
            "IntentResolutionStep",
            "SchemaExtractionStep",
            "RulesStep",
            "ResponseResolutionStep"
    );

    private final DemoTransportProperties demoTransportProperties;

    @Override
    public boolean supports(String stepName, EngineSession session) {
        return demoTransportProperties.getStepHook().isEnabled() && TRACKED_STEPS.contains(stepName);
    }

    @Override
    public void beforeStep(String stepName, EngineSession session) {
        log.info(
                "[StepHook][before] step={} convId={} intent={} state={} userText={}",
                stepName,
                session.getConversationId(),
                session.getIntent(),
                session.getState(),
                abbreviate(session.getUserText())
        );
    }

    @Override
    public void afterStep(String stepName, EngineSession session, StepResult result) {
        log.info(
                "[StepHook][after] step={} convId={} result={} intent={} state={} locked={}",
                stepName,
                session.getConversationId(),
                result.getClass().getSimpleName(),
                session.getIntent(),
                session.getState(),
                session.isIntentLocked()
        );
    }

    @Override
    public void onStepError(String stepName, EngineSession session, Throwable error) {
        log.error(
                "[StepHook][error] step={} convId={} intent={} state={} message={}",
                stepName,
                session.getConversationId(),
                session.getIntent(),
                session.getState(),
                error == null ? null : error.getMessage(),
                error
        );
    }

    private String abbreviate(String text) {
        if (text == null) {
            return null;
        }
        return text.length() <= 120 ? text : text.substring(0, 120) + "...";
    }
}
