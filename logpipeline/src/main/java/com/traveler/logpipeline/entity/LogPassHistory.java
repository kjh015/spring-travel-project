package com.traveler.logpipeline.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "log_pass_history", uniqueConstraints = @UniqueConstraint(columnNames = {"deduplication_id", "user_id"}))
public class LogPassHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "process_id")
    private Process process;

    @ManyToOne
    @JoinColumn(name = "deduplication_id")
    private Deduplication deduplication;

    private String userId;

    @CreationTimestamp
    private LocalDateTime createdTime;

    private LocalDateTime expiredTime;

}
