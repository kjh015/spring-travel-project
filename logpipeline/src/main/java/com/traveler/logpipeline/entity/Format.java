package com.traveler.logpipeline.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
public class Format {
    @Id
    private String processId;

    @Column(columnDefinition = "TEXT")
    private String formatJson;

    private LocalDateTime updatedTime;
}
