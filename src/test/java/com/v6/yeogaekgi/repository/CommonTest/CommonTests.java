package com.v6.yeogaekgi.repository.CommonTest;

import com.v6.yeogaekgi.member.entity.Country;
import com.v6.yeogaekgi.member.entity.Gender;
import com.v6.yeogaekgi.member.entity.Member;
import com.v6.yeogaekgi.member.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class CommonTests {
    @Autowired
    protected MemberRepository memberRepository;

    protected Member member;

    @BeforeEach
    void setUp() {
        Country country = new Country("KR", "South Korea");
        member = new Member(
                "test@example.com",
                "testPassword",
                "testName",
                "testNickname",
                Gender.MALE,
                "010-1234-5678",
                "12345678910",
                country
        );
        memberRepository.save(member);
    }
}
