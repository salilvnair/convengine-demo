package com.github.salilvnair.convengdemo.bean.service;

import com.github.salilvnair.convengdemo.repo.FaqRepository;
import com.github.salilvnair.convengine.llm.core.LlmClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class FaqEmbeddingJob {

    private final FaqRepository faqRepo;
    private final LlmClient llm;

    @Transactional
    public void run() {

        List<Map<String, Object>> faqs = faqRepo.findFaqsWithoutEmbedding();

        for (Map<String, Object> row : faqs) {

            long faqId = ((Number) row.get("faq_id")).longValue();

            // ✅ EMBED QUESTION ONLY
            String question = (String) row.get("question");

            if (question == null || question.isBlank()) {
                throw new IllegalStateException("FAQ " + faqId + " has empty question");
            }

            float[] emb = llm.generateEmbedding(question);

            validateEmbedding(emb, faqId);

            faqRepo.updateEmbedding(faqId, toPgVector(emb));
        }
    }

    // -----------------------------
    // HARD SAFETY — DO NOT REMOVE
    // -----------------------------
    private static void validateEmbedding(float[] emb, long faqId) {

        if (emb == null || emb.length == 0) {
            throw new IllegalStateException("Embedding missing for FAQ " + faqId);
        }

        float norm = 0f;
        for (float v : emb) {
            norm += v * v;
        }
        norm = (float) Math.sqrt(norm);

        if (norm < 0.01f) {
            throw new IllegalStateException(
                    "Near-zero embedding detected for FAQ " + faqId
            );
        }
    }

    public static String toPgVector(float[] emb) {
        StringBuilder sb = new StringBuilder();
        sb.append('[');
        for (int i = 0; i < emb.length; i++) {
            if (i > 0) sb.append(',');
            sb.append(emb[i]);
        }
        sb.append(']');
        return sb.toString();
    }
}
