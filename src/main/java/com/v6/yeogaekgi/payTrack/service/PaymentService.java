package com.v6.yeogaekgi.payTrack.service;

import com.v6.yeogaekgi.payTrack.dto.PaymentDTO;
import com.v6.yeogaekgi.payTrack.entity.Payment;
import com.v6.yeogaekgi.payTrack.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private final PaymentRepository paymentRepository;

    private PaymentDTO findById(Long id) {
        Optional<Payment> paymentHistory = paymentRepository.findById(id);

        paymentHistory.ifPresent(payment -> {
            return;
        });

        return null;
    }

    private Payment dtoToEntity(PaymentDTO dto) {
        return Payment.builder()

                .build();
    }

    private Payment entityToDto(Payment payment) {
        return Payment.builder()

                .build();
    }


}
