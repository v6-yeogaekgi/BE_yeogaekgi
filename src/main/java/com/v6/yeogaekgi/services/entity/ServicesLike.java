package com.v6.yeogaekgi.services.entity;

import com.v6.yeogaekgi.member.entity.Member;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Table(name = "services_like")
public class ServicesLike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "services_like_no")
    private Long no;

    @ManyToOne
    @JoinColumn(name ="service_no", nullable = false)
    private Services services;

    @ManyToOne
    @JoinColumn(name = "member_no", nullable = false)
    private Member member;

}
