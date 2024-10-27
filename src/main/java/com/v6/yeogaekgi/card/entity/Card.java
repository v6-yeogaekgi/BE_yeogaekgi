package com.v6.yeogaekgi.card.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Table(name = "card")
public class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name ="card_no")
    private Long no;

    @Column
    private String design;

    @Column(name="card_name", nullable=false)
    private String cardName;

    @Column(nullable=false)
    private String area;
}
