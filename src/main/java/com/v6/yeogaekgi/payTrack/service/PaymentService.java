package com.v6.yeogaekgi.payTrack.service;

import com.v6.yeogaekgi.card.entity.UserCard;
import com.v6.yeogaekgi.member.entity.Member;
import com.v6.yeogaekgi.payTrack.dto.PaymentDTO;
import com.v6.yeogaekgi.payTrack.entity.Payment;
import com.v6.yeogaekgi.payTrack.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class PaymentService {
    private final PaymentRepository paymentRepository;

    public PaymentDTO findById(Long id, Long memberNo) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Payment not found"));

        if (!payment.getMember().getId().equals(memberNo)) {
            throw new AccessDeniedException("You don't have permission to view this payment");
        }

        return entityToDto(payment);
    }


    private Payment dtoToEntity(PaymentDTO dto) {
        return Payment.builder()
                .id(dto.getPayNo())
                .payType(dto.getPayType())
                .payPrice(dto.getPayPrice())
                .payBalanceSnap(dto.getPayBalanceSnap())
                .transitBalanceSnap(dto.getTransitBalanceSnap())
                .payDate(dto.getPayDate())
                .status(dto.getStatus())
                .serviceName(dto.getServiceName())
                .userCard( UserCard.builder().id(dto.getUserCardNo()).build() )
                .member( Member.builder().id(dto.getMemberNo()).build() )
                .build();
    }

    private PaymentDTO entityToDto(Payment payment) {
        return PaymentDTO.builder()
                .payNo(payment.getId())
                .payType(payment.getPayType())
                .payPrice(payment.getPayPrice())
                .payBalanceSnap(payment.getPayBalanceSnap())
                .transitBalanceSnap(payment.getTransitBalanceSnap())
                .payDate(payment.getPayDate())
                .status(payment.getStatus())
                .serviceName(payment.getServiceName())
                .userCardNo(payment.getUserCard().getId())
                .memberNo(payment.getMember().getId())
                .build();
    }


}
