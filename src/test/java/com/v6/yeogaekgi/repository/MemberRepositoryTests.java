package com.v6.yeogaekgi.repository;



import com.v6.yeogaekgi.community.entity.Comment;
import com.v6.yeogaekgi.community.entity.Post;
import com.v6.yeogaekgi.member.entity.Country;
import com.v6.yeogaekgi.member.entity.Gender;
import com.v6.yeogaekgi.member.entity.Member;
import com.v6.yeogaekgi.member.repository.CountryRepository;
import com.v6.yeogaekgi.member.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.stream.IntStream;

@SpringBootTest
public class MemberRepositoryTests {

    @Autowired
    private CountryRepository countryRepository;
    @Autowired
    private MemberRepository memberRepository;


    @Test
    public void insertMember() {
        // Country가 없으므로 테스트용 Country 데이터를 미리 추가합니다.
        Country country = Country.builder()
                .code("KR") // 한국 코드를 예로 들었습니다.
                .name("Korea")
                .build();

        // Country 저장
        countryRepository.save(country);

        // 200개의 Member 더미 데이터를 등록
        IntStream.rangeClosed(1, 200).forEach(i -> {
            // 랜덤한 이메일, 비밀번호, 이름, 닉네임, 전화번호, 여권번호 생성
            String email = "user" + i + "@test.com";
            String password = "password" + i;
            String name = "User" + i;
            String nickname = "Nick" + i;
            Gender gender = i % 2 == 0 ? Gender.MALE : Gender.FEMALE; // 짝수는 남성, 홀수는 여성으로 설정
            String phoneNumber = "010-1234-" + String.format("%04d", i);
            String passport = "P" + String.format("%06d", i);

            // Member 객체 생성
            Member member = new Member(email, password, name, nickname, gender, phoneNumber, passport, country);

            // Member 저장
            memberRepository.save(member);
        });
    }
}
