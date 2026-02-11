package com.github.salilvnair.convengdemo.repo;

import com.github.salilvnair.convengdemo.entity.ZpFaq;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface FaqRepository extends JpaRepository<ZpFaq, Long> {

    /**
     * Fetch FAQs without embeddings (native because of vector column)
     */
    @Query(
            value = """
                SELECT
                    faq_id,
                    question,
                    answer
                FROM zp_faq
                WHERE embedding IS NULL
                  AND enabled = true
                """,
            nativeQuery = true
    )
    List<Map<String, Object>> findFaqsWithoutEmbedding();

    /**
     * Update pgvector embedding
     */
    @Modifying
    @Query(
            value = """
                UPDATE zp_faq
                SET embedding = CAST(:embedding AS vector)
                WHERE faq_id = :faqId
                """,
            nativeQuery = true
    )
    void updateEmbedding(long faqId, String embedding);
}
