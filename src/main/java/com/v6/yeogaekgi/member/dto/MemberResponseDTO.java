package com.v6.yeogaekgi.member.dto;

import com.v6.yeogaekgi.member.entity.Country;
import com.v6.yeogaekgi.member.entity.Gender;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MemberResponseDTO {
    private String email;
    private String name;
    private String nickname;
    private Gender gender;
    private String phoneNumber;
    private String passport;
    private String accountNumber;
    private String bank;
    private Country country;

    public MemberResponseDTO(String email, String name, String nickname, Gender gender, String phoneNumber, String passport,
                             String accountNumber, String bank, Country country){
        this.email = email;
        this.name = name;
        this.nickname = nickname;
        this.gender = gender;
        this.phoneNumber=phoneNumber;
        this.passport=passport;
        this.accountNumber=accountNumber;
        this.bank=bank;
        this.country=country;
    }
}
