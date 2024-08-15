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
    @Column(name ="comment_no")
    private Long id;

    @Column(nullable=false)
    private String comment;

    public void changeComment(String comment) {this.comment = comment;}

    @ManyToOne
    @JoinColumn(name = "post_no", nullable = false)
    private Post post;

//    @ManyToOne
//    private Member member;
}


