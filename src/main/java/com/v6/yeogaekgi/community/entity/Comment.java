package com.v6.yeogaekgi.community.entity;

import com.v6.yeogaekgi.util.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Table(name = "comment")
public class Comment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long comment_no;
    private String comment;

    @ManyToOne
    private Post post;

//    @ManyToOne
//    private Member member;
}


