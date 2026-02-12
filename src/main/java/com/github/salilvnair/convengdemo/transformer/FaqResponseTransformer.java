package com.github.salilvnair.convengdemo.transformer;

import com.github.salilvnair.convengine.engine.response.annotation.ResponseTransformer;
import com.github.salilvnair.convengine.engine.response.transformer.ResponseTransformerHandler;
import com.github.salilvnair.convengine.engine.session.EngineSession;
import com.github.salilvnair.convengine.model.JsonPayload;
import com.github.salilvnair.convengine.model.OutputPayload;
import com.github.salilvnair.convengine.model.TextPayload;
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
        if(responsePayload instanceof TextPayload) {
           return new TextPayload("FaqResponseTransformer transformed TextPayload response!");
        }
        if(responsePayload instanceof JsonPayload) {
            return new JsonPayload("""
                    {
                      "answer": "FaqResponseTransformer transformed JsonPayload response!",
                      "confidence": 1.0,
                      "matchedFaqIds": [53],
                      "needsClarification": false
                    }
                    """
            );
        }
        return null;
    }
}
