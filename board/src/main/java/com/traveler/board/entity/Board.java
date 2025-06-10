package com.traveler.board.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
public class Board {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "travel_place_id")
    private TravelPlace travelPlace;

    private Long memberId;

    @Column(length=100)
    private String title;

    @Column(length=2000)
    private String content;

    private Double ratingAvg;

    private Long viewCount;

    private Long favoriteCount;

    private Long commentCount;

    @CreationTimestamp
    private LocalDateTime regDate;

    @UpdateTimestamp
    private LocalDateTime modifiedDate;




}
