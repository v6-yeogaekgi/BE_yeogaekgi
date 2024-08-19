package com.v6.yeogaekgi.member.repository;

import com.v6.yeogaekgi.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member,Long> {
    Optional<Member> findByNickname(String nickname);
}
