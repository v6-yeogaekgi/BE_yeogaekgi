package com.v6.yeogaekgi.member.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "refreshtoken_id")
    private Long id;
    private String token;
    private String username;

    public RefreshToken(String token,String username){
        this.token = token;
        this.username = username;
    }

    public void updateToken(String token) {
        this.token = token;
    }
}
