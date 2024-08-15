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
    @Column(name ="qna_no")
    private int id;

    @Column(nullable=false)
    private String title;

    @Column(nullable=false)
    private String content;

    @Column
    private String img;

    @Column(nullable=false)
    @CurrentTimestamp
    private Timestamp qna_date;

    @Column
    private String reply;

    @Column
    private Timestamp reply_date;

    @Column(nullable=false)
    private int status;
}
