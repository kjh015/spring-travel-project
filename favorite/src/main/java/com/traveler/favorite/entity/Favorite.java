package com.traveler.favorite.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

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
