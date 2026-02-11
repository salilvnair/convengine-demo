package com.github.salilvnair.convengdemo.controller;

import com.github.salilvnair.convengdemo.bean.service.FaqEmbeddingJob;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/internal/faq")
@RequiredArgsConstructor
public class FaqEmbeddingController {

    private final FaqEmbeddingJob embeddingJob;

    /**
     * One-time (or re-run) embedding population for zp_faq table.
     *
     * Call manually or from admin UI / cron / job runner.
     */
    @PostMapping("/embed")
    public ResponseEntity<String> generateEmbeddings() {

        embeddingJob.run();

        return ResponseEntity.ok(
                "FAQ embeddings generated successfully"
        );
    }
}
