package com.v6.yeogaekgi.member.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

@Builder
@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name ="member_no")
    private Long no;

    @Column(nullable=false)
    private String email;

    @Column(nullable=false)
    private String password;

    @Column(nullable=false)
    private String name;

    @Column(nullable=false)
    private String nickname;

    @Column(nullable=false)
    private Gender gender;

    @Column(name="phone_number",nullable = false)
    private String phoneNumber;

    @Column(nullable = false)
    private String passport;

    @Column(name="account_number")
    private String accountNumber;

    @Column
    private String bank;

    private Boolean isDeleted = false;

    @ManyToOne
    @JoinColumn(name = "code", nullable = false)
    private Country country;

    public Member(String email,String password, String name, String nickname, Gender gender, String phoneNumber, String passport, Country country){
        this.email = email;
        this.password = password;
        this.name = name;
        this.nickname = nickname;
        this.gender = gender;
        this.phoneNumber = phoneNumber;
        this.passport = passport;
        this.country = country;
    }
    public Member(String email,String password, String name, String nickname, Gender gender, String phoneNumber, String passport, Country country, String bank, String accountNumber){
        this.email = email;
        this.password = password;
        this.name = name;
        this.nickname = nickname;
        this.gender = gender;
        this.phoneNumber = phoneNumber;
        this.passport = passport;
        this.country = country;
        this.bank = bank;
        this.accountNumber = accountNumber;
    }
}
