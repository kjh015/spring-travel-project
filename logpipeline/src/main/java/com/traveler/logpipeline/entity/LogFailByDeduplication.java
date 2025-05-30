package com.traveler.logpipeline.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
public class LogFailByDeduplication {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "process_id")
    private Process process;

    @Column(columnDefinition = "TEXT")
    private String logJson;

    @ManyToOne
    @JoinColumn(name = "deduplication_id")
    private Deduplication deduplication;

    @CreationTimestamp
    private LocalDateTime createdTime;
}
