package com.traveler.favorite.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "favorite", uniqueConstraints = @UniqueConstraint(columnNames = {"board_id", "member_nickname"}))
public class Favorite {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long boardId;

    private Long memberId;

    @CreationTimestamp
    private LocalDateTime createdTime;
}
