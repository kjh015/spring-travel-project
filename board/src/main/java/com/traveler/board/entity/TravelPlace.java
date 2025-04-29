package com.traveler.board.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
public class TravelPlace {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "region_id")
    private Region region;

    private String name;

    private String address;
}
