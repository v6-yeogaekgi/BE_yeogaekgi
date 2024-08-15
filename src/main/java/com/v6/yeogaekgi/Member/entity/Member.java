package com.v6.yeogaekgi.Member.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name ="member_no")
    private Long id;

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
    @Column(name="account_number",nullable = true)
    private String accountNumber;
    @Column(nullable=true)
    private String bank;
    private Boolean isDeleted = false;
    @ManyToOne
    @JoinColumn(name = "code", nullable = false)
    private Country country;

}
