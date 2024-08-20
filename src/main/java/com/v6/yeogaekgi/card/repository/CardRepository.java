package com.v6.yeogaekgi.card.repository;

import com.v6.yeogaekgi.card.entity.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CardRepository extends JpaRepository<Card, Long> {
    @Query("""
    SELECT c
    FROM UserCard u
    INNER JOIN Card c
    ON c.id = u.id
    WHERE u.member.id = :memberNo
    """)
    List<Card> findCardByMemberNo(@Param("memberNo") Long memberNo);
}
