package com.github.salilvnair.convengdemo.llm.provider.lmstudio.context;

import com.github.salilvnair.convengine.engine.session.EngineSession;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class LmStudioEmbeddingApiContext {
    private EngineSession session;

    private String model;
    private String input;

    // output
    private float[] embedding;
}
