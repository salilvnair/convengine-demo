package com.github.salilvnair.convengdemo.llm.provider.lmstudio.context;

import com.github.salilvnair.convengdemo.llm.provider.openai.model.OpenAiRequest;
import com.github.salilvnair.convengine.llm.base.type.OutputType;
import com.github.salilvnair.convengdemo.llm.provider.openai.model.OpenAiResponse;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class LmStudioApiContext {
    private String model;
    private String hint;
    private String userContext;
    private String jsonSchema;
    private OutputType type;
    private boolean strictJson;

    private OpenAiResponse response;
    private List<OpenAiRequest.Message> messages;
}