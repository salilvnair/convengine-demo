package com.github.salilvnair.convengdemo.transformer;

import com.github.salilvnair.convengine.engine.response.annotation.ResponseTransformer;
import com.github.salilvnair.convengine.engine.response.transformer.ResponseTransformerHandler;
import com.github.salilvnair.convengine.engine.session.EngineSession;
import com.github.salilvnair.convengine.model.OutputPayload;
import org.springframework.stereotype.Component;
import java.util.Map;

@Component
@ResponseTransformer(
        intent = "FAQ",
        state = "IDLE"
)
public class FaqResponseTransformer implements ResponseTransformerHandler {

    @Override
    public OutputPayload transform(OutputPayload responsePayload, EngineSession session, Map<String, Object> inputParams) {
        return responsePayload;
    }
}
