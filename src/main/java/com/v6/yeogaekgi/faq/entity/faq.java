package com.v6.yeogaekgi.faq.entity;

import com.v6.yeogaekgi.community.entity.Post;
import com.v6.yeogaekgi.util.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CurrentTimestamp;

import java.sql.Timestamp;

@Getter
@Setter
@Entity
@Table(name = "faq")
public class faq extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int faq_no;
    private String title;
    private String content;


}
