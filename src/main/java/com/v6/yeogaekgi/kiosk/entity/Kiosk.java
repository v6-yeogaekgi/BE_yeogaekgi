package com.v6.yeogaekgi.kiosk.entity;

import com.v6.yeogaekgi.member.entity.Member;
import com.v6.yeogaekgi.util.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Kiosk extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name ="kiosk_no")
    private long id;

    @Column(nullable = false)
    private String location;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    @ColumnDefault("0")
    private int balance;

    @Column(nullable = false)
    @ColumnDefault("0")
    private int amount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_no", nullable = false)
    private Member member;

    public Kiosk(String location, String address, int balance, int amount, Member member) {
        this.location = location;
        this.address = address;
        this.balance = balance;
        this.amount = amount;
        this.member = member;
    }
}
