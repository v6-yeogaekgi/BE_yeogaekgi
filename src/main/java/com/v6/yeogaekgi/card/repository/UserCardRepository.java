package com.v6.yeogaekgi.card.repository;

import com.v6.yeogaekgi.card.entity.UserCard;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserCardRepository extends JpaRepository<UserCard, Long> {
    List<UserCard> findByMemberNo(Long memberNo);

    boolean existsByNoAndMemberNo(Long userCardNo, Long memberNo);
}
