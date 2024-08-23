package com.v6.yeogaekgi.community.entity;

import com.v6.yeogaekgi.member.entity.Member;
import com.v6.yeogaekgi.util.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CurrentTimestamp;

import java.sql.Timestamp;
@ToString
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Table(name = "post")
public class Post extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name ="post_no")
    private Long id;

    @Column(nullable=false)
    private String content;

    private String images;

    private String hashtag;

    @Column(name="like_cnt", nullable = false)
    @ColumnDefault("0")
    private int likeCnt;

    @Column(name="comment_cnt",nullable = false)
    @ColumnDefault("0")
    private int commentCnt;

    @ManyToOne
    @JoinColumn(name = "member_no", nullable = false)
    private Member member;

    public void changeContent(String comment) {this.content = comment;}
    public void changeImages(String images) {this.images = images;}
    public void changeHashtag(String hashtag) {this.hashtag = hashtag;}
    public void changeLikeCnt(int likeCnt) {this.likeCnt = likeCnt;}

}