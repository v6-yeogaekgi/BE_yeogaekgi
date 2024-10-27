package com.v6.yeogaekgi.services.entity;

import com.v6.yeogaekgi.member.entity.Country;
import com.v6.yeogaekgi.payTrack.entity.Payment;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
@Builder
public class Services {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "service_no")
    private Long no;

    @Column(nullable = false)
    private ServicesType type;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private String address;

    @Column(name = "start_contact")
    private LocalDate startContact;

    @Column(name = "end_contact")
    private LocalDate  endContact;

    @Column(nullable = false,precision = 9,scale = 6)
    private BigDecimal lat;

    @Column(nullable = false,precision =10,scale = 6)
    private BigDecimal lon;

    @Column(name = "like_cnt")
    private int likeCnt;

    public void incrementLikeCnt() {
        this.likeCnt += 1;
    }

    public void decreaseLikeCnt(){
        this.likeCnt -= 1;
    }


}
