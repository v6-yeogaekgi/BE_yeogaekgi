package com.v6.yeogaekgi.payTrack.repository;

import com.v6.yeogaekgi.payTrack.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Optional<Payment> findById(Long id);
}
