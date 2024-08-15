package com.v6.yeogaekgi.community.entity;

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
    private Long post_like_no;

    @ManyToOne
    private Post post;

//    @ManyToOne
//    private Member member;
}
