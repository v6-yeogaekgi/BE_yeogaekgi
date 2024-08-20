package com.v6.yeogaekgi.payTrack.repository;

import com.v6.yeogaekgi.payTrack.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    Optional<Transaction> findById(Long tranId);
}
