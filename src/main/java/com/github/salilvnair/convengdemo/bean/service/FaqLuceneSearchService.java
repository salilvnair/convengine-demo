package com.github.salilvnair.convengdemo.bean.service;


import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.index.*;
import org.apache.lucene.search.*;
import org.apache.lucene.store.ByteBuffersDirectory;
import org.apache.lucene.store.Directory;
import org.springframework.stereotype.Service;
import java.util.*;

@Service("faqLuceneSearchService")
public class FaqLuceneSearchService {

    private static final int MAX_RESULTS = 5;

    /**
     * Entry point called by CCF via bean + method.
     *
     * @param faqQuery user text (free form)
     * @param dbData   FAQ rows from DB (UPPERCASE column names)
     */
    public List<Map<String, Object>> searchFaqs(
            String faqQuery,
            List<Map<String, Object>> dbData
    ) {

        if (faqQuery == null || faqQuery.isBlank() || dbData == null || dbData.isEmpty()) {
            return List.of();
        }

        try {
            Analyzer analyzer = new StandardAnalyzer();
            Directory directory = new ByteBuffersDirectory();

            // ----------------------------
            // Build index
            // ----------------------------
            IndexWriterConfig config = new IndexWriterConfig(analyzer);
            try (IndexWriter writer = new IndexWriter(directory, config)) {
                for (Map<String, Object> row : dbData) {
                    writer.addDocument(toDocument(row));
                }
            }

            // ----------------------------
            // Search
            // ----------------------------
            try (DirectoryReader reader = DirectoryReader.open(directory)) {
                IndexSearcher searcher = new IndexSearcher(reader);

                Query query = buildFuzzyQuery(faqQuery, analyzer);

                TopDocs hits = searcher.search(query, MAX_RESULTS);

                List<Map<String, Object>> results = new ArrayList<>();

                for (ScoreDoc sd : hits.scoreDocs) {
                    Document doc = searcher.storedFields().document(sd.doc);


                    Map<String, Object> result = new LinkedHashMap<>();
                    result.put("faqId", Long.valueOf(doc.get("faqId")));
                    result.put("question", doc.get("question"));
                    result.put("answer", doc.get("answer"));
                    result.put("category", doc.get("category"));
                    result.put("tags", doc.get("tags"));

                    // normalize Lucene score → confidence [0..1]
                    result.put("confidence", normalizeScore(sd.score));

                    results.add(result);
                }

                return results;
            }

        } catch (Exception e) {
            throw new IllegalStateException("Lucene FAQ search failed", e);
        }
    }

    // ------------------------------------------------------------------
    // Helpers
    // ------------------------------------------------------------------

    private Document toDocument(Map<String, Object> row) {
        Document doc = new Document();

        doc.add(new StringField(
                "faqId",
                String.valueOf(row.get("FAQ_ID")),
                Field.Store.YES
        ));

        doc.add(new TextField(
                "question",
                safe(row.get("QUESTION")),
                Field.Store.YES
        ));

        doc.add(new TextField(
                "answer",
                safe(row.get("ANSWER")),
                Field.Store.YES
        ));

        doc.add(new TextField(
                "category",
                safe(row.get("CATEGORY")),
                Field.Store.YES
        ));

        doc.add(new TextField(
                "tags",
                safe(row.get("TAGS")),
                Field.Store.YES
        ));

        return doc;
    }

    private Query buildFuzzyQuery(String input, Analyzer analyzer) {

        BooleanQuery.Builder builder = new BooleanQuery.Builder();

        // fuzzy on question
        builder.add(
                new FuzzyQuery(new Term("question", normalize(input)), 2),
                BooleanClause.Occur.SHOULD
        );

        // fuzzy on tags
        builder.add(
                new FuzzyQuery(new Term("tags", normalize(input)), 2),
                BooleanClause.Occur.SHOULD
        );

        // optional: category boost
        builder.add(
                new FuzzyQuery(new Term("category", normalize(input)), 1),
                BooleanClause.Occur.SHOULD
        );

        return builder.build();
    }

    private double normalizeScore(float score) {
        // empirical normalization for UI confidence badge
        return Math.min(1.0, score / 5.0);
    }

    private String normalize(String s) {
        return s.toLowerCase(Locale.ROOT).trim();
    }

    private String safe(Object o) {
        return o == null ? "" : String.valueOf(o);
    }
}
