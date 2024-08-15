package com.v6.yeogaekgi.Member.repository;

import com.v6.yeogaekgi.Member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member,Long> {
}
