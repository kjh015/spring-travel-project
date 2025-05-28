package com.traveler.comment.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long boardId;

    private String memberNickname;

    @Column(length=500)
    private String content;

    private Integer rating; //별점

    @CreationTimestamp
    private LocalDateTime createdTime;

}
