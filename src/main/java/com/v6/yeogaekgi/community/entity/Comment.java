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
    private Long no;

    @Column(nullable=false)
    private String content;

    public void changeComment(String content) {this.content = content;}

    @ManyToOne
    @JoinColumn(name = "post_no", nullable = false)
    private Post post;

    @ManyToOne
    @JoinColumn(name = "member_no", nullable = false)
    private Member member;
}