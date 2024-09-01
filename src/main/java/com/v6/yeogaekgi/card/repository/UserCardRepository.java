package com.v6.yeogaekgi.card.repository;

import com.v6.yeogaekgi.card.entity.UserCard;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserCardRepository extends JpaRepository<UserCard, Long> {
    List<UserCard> findByMember_Id(Long memberId);

    boolean existsByIdAndMember_Id(Long id, Long memberId);
}
