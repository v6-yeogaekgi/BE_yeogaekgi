package com.v6.yeogaekgi.community.entity;

import com.v6.yeogaekgi.member.entity.Member;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Table(name = "post_like")
public class PostLike {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name ="post_like_no")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "post_no", nullable = false)
    private Post post;

    @ManyToOne
    @JoinColumn(name = "member_no", nullable = false)
    private Member member;
}