package com.v6.yeogaekgi.card.repository;

import com.v6.yeogaekgi.card.entity.UserCard;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserCardRepository extends JpaRepository<UserCard, Long> {
}
