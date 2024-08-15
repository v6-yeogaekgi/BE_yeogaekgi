package com.v6.yeogaekgi.qna.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CurrentTimestamp;

import java.sql.Timestamp;

@Getter
@Setter
@Entity
@Table(name = "qna")
public class qna {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int qna_no;
    private String title;
    private String content;
    private String img;
    @CurrentTimestamp
    private Timestamp qna_date;
    private String reply;
    private Timestamp reply_date;
    private int status;
}
