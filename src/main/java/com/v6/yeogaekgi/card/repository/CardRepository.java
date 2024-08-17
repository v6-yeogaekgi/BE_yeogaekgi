package com.v6.yeogaekgi.card.repository;

import com.v6.yeogaekgi.card.entity.Card;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CardRepository extends JpaRepository<Card, Long> {
}
