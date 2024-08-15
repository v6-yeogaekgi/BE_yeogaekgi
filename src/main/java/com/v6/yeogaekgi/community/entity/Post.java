package com.v6.yeogaekgi.community.entity;

import com.v6.yeogaekgi.util.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CurrentTimestamp;

import java.sql.Timestamp;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Table(name = "post")
public class Post extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long post_no;
    private String title;
    private String content;
    private String images;
    private String hashtag;
    private int like_cnt;
//    private String filename_org;
//    private String filename_real;
    private int comment_cnt;

//    @ManyToOne
//    private Member member;


}
