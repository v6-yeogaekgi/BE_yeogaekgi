package com.v6.yeogaekgi.Member.dto;

import com.v6.yeogaekgi.Member.entity.Gender;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MemberRequestDto {
    private String email;
    private String password;
    private String confirmPassword;
    private String name;
    private String nickname;
    private Gender gender;
    @Pattern(regexp = "^(\\d{3})-(\\d{4})-(\\d{4})$")
    private String phoneNumber;
    private String passport;
    private String code;
}
