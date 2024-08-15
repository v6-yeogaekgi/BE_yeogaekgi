package com.v6.yeogaekgi.community.entity;

import com.v6.yeogaekgi.util.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "comment")
public class Comment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int comment_no;
    private String comment;

    @ManyToOne
    private Post post;
}


