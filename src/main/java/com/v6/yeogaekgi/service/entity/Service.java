package com.v6.yeogaekgi.service.entity;

import com.v6.yeogaekgi.util.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class Service {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "service_no")
    private Long id;

    @Column(nullable = false)
    private ServiceType type;

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
}
