package com.v6.yeogaekgi.member.repository;

import com.v6.yeogaekgi.member.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken,Long> {
    Optional<RefreshToken> findByEmail(String email);
    boolean existsByToken(String refresh);
}
