package com.v6.yeogaekgi.review.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_no")
    private Long id;

    private String images;

    @Column(nullable=false)
    private int score;

    @Column(nullable=false)
    private String content;

    @Column(nullable=false)
    private int status=0;

}
