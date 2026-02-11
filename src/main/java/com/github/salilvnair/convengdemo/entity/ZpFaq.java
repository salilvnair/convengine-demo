package com.github.salilvnair.convengdemo.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.OffsetDateTime;

@Entity
@Table(name = "zp_faq")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ZpFaq {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "faq_id")
    private Long faqId;

    @Column(name = "category")
    private String category;

    @Column(name = "question", nullable = false)
    private String question;

    @Column(name = "answer", nullable = false)
    private String answer;

    @Column(name = "tags")
    private String tags;

    @Column(name = "enabled")
    private Boolean enabled;

    @Column(name = "priority")
    private Integer priority;

    @Column(name = "embedding", columnDefinition = "vector(1536)")
    private String embedding; // pgvector stored as text literal

    @Column(name = "created_at")
    private OffsetDateTime createdAt;
}
