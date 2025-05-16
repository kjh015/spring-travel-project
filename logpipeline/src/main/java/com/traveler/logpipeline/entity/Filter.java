package com.traveler.logpipeline.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
public class Filter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "process_id")
    private Process process;

    private String name;

    @Lob
    @Column(columnDefinition = "LONGTEXT")
    private String sourceCode;

    @Column(columnDefinition = "TEXT")
    private String tokensJson;

    @Column(columnDefinition = "TEXT")
    private String usedField;

    private boolean isActive;

    @CreationTimestamp
    private LocalDateTime createdTime;

    @UpdateTimestamp
    private LocalDateTime updatedTime;
}
