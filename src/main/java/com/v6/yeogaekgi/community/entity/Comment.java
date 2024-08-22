package com.v6.yeogaekgi.community.entity;

import com.v6.yeogaekgi.member.entity.Member;
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
    @Column(name ="comment_no")
    private Long id;

    @Column(nullable=false)
    private String content;

    public void changeComment(String comment) {this.content = comment;}

    @ManyToOne
    @JoinColumn(name = "post_no", nullable = false)
    private Post post;

    @ManyToOne
    @JoinColumn(name = "member_no", nullable = false)
    private Member member;
}