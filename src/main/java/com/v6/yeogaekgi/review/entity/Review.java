package com.v6.yeogaekgi.review.entity;

import com.v6.yeogaekgi.member.entity.Member;
import com.v6.yeogaekgi.service.entity.Service;
import com.v6.yeogaekgi.util.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString(exclude = {"service","member"})
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "service_no", nullable = false)
    private Service service;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_no", nullable = false)
    private Member member;



}
