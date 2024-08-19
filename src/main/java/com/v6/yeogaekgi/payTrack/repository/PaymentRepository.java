package com.v6.yeogaekgi.payTrack.repository;

import com.v6.yeogaekgi.payTrack.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
}
