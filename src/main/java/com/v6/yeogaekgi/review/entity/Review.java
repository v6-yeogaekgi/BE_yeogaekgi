package com.v6.yeogaekgi.review.entity;

import com.v6.yeogaekgi.util.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Review extends BaseEntity {
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
