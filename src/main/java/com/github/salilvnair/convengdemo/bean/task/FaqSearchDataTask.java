package com.github.salilvnair.convengdemo.bean.task;

import com.github.salilvnair.ccf.core.data.context.DataTaskContext;
import com.github.salilvnair.ccf.core.data.handler.task.base.DataTask;
import com.github.salilvnair.convengdemo.bean.service.FaqEmbeddingJob;
import com.github.salilvnair.convengine.llm.core.LlmClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


@Component("faqSearchDataTask")
@RequiredArgsConstructor
public class FaqSearchDataTask implements DataTask  {

    private final LlmClient llm;

    public void injectFaqQueryEmbedding(DataTaskContext dataTaskContext) {
        String faqQuery = (String) dataTaskContext.dataContext().getInputParams().get("faqQuery");
        float[] floats = llm.generateEmbedding(faqQuery);
        String pgVector = FaqEmbeddingJob.toPgVector(floats);
        dataTaskContext.dataContext().getInputParams().put("faqQueryEmbedding", pgVector);
    }
}
