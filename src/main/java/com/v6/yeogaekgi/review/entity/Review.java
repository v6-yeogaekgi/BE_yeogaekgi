package com.v6.yeogaekgi.review.entity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.v6.yeogaekgi.member.entity.Member;
import com.v6.yeogaekgi.payTrack.entity.Payment;
import com.v6.yeogaekgi.review.dto.ReviewUpdateDTO;
import com.v6.yeogaekgi.services.entity.Services;
import com.v6.yeogaekgi.util.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString(exclude = {"services","member","payment"})
public class Review extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_no")
    private Long id;

    @Column(columnDefinition = "LONGTEXT")
    private String images;

    @Column(columnDefinition = "LONGTEXT")
    private String thumbnails;

    @Column(nullable=false)
    private int score;

    @Column(nullable=false)
    private String content;

    @Column(nullable=false)
    @ColumnDefault("0")
    private int status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "service_no", nullable = false)
    private Services services;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_no", nullable = false)
    private Member member;


    @OneToOne(fetch = FetchType.LAZY,optional = true)
    @JoinColumn(name = "pay_no")
    private Payment payment;

    public List<String> getImages() {
        return convertStringToList(this.images);
    }

    public void setImages(List<String> images) {
        this.images = convertListToString(images);
    }

    public List<String> getThumbnails() {
        return convertStringToList(this.thumbnails);
    }

    public void setThumbnails(List<String> thumbnails) {
        this.thumbnails = convertListToString(thumbnails);
    }

    private List<String> convertStringToList(String jsonString) {
        if (jsonString == null || jsonString.isEmpty()) {
            return new ArrayList<>();
        }
        try {
            return new ObjectMapper().readValue(jsonString, new TypeReference<List<String>>() {});
        } catch (JsonProcessingException e) {
            throw new RuntimeException("변환 중 오류 발생", e);
        }
    }

    private String convertListToString(List<String> list) {
        try {
            return new ObjectMapper().writeValueAsString(list);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("변환 중 오류 발생", e);
        }
    }

    public void update(ReviewUpdateDTO reviewUpdateDTO, List<String> images, List<String> thumbnails, int score, String content) {
        this.images = convertListToString(images);
        this.thumbnails = convertListToString(thumbnails);
        this.score = score;
        this.content = content;
    }
}
