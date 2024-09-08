package com.v6.yeogaekgi.payTrack.service;

import com.v6.yeogaekgi.card.entity.UserCard;
import com.v6.yeogaekgi.card.repository.UserCardRepository;
import com.v6.yeogaekgi.member.entity.Member;
import com.v6.yeogaekgi.member.repository.MemberRepository;
import com.v6.yeogaekgi.payTrack.dto.PaymentDTO;
import com.v6.yeogaekgi.payTrack.entity.Payment;
import com.v6.yeogaekgi.payTrack.repository.PaymentRepository;
import com.v6.yeogaekgi.review.entity.Review;
import com.v6.yeogaekgi.review.repository.ReviewRepository;
import com.v6.yeogaekgi.services.entity.Services;
import com.v6.yeogaekgi.services.repository.ServicesRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Log4j2
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final UserCardRepository userCardRepository;
    private final ServicesRepository servicesRepository;
    private final ReviewRepository reviewRepository;

    @Transactional
    public boolean insertPayment(PaymentDTO paymentDTO) {
        Optional<UserCard> userCard = userCardRepository.findById(paymentDTO.getUserCardNo());

        if (!userCard.isPresent()) {
            return false;
        }
        UserCard prevUserCard = userCard.get();

        int payBalance = prevUserCard.getPayBalance();
        int transitBalance = prevUserCard.getTransitBalance();


        if(paymentDTO.getPayType() == 1){
            transitBalance -= paymentDTO.getPayPrice();
        }else{
            payBalance -= paymentDTO.getPayPrice();
        }

        prevUserCard.updatePayBalance(payBalance);
        prevUserCard.updateTransitBalance(transitBalance);
        userCardRepository.save(prevUserCard);

        if(paymentDTO.getServiceNo() != null) {
            Optional<Services> service = servicesRepository.findById(paymentDTO.getServiceNo());
            if (service.isPresent()) {
                paymentDTO.setServiceName(service.get().getName());
            } else {
                log.warn("Service not found for serviceNo: {}", paymentDTO.getServiceNo());
                paymentDTO.setServiceName("Unknown Service");
            }
        }

        paymentRepository.save(dtoToEntity(paymentDTO));

        return true;
    }

    public List<PaymentDTO> findPaymentsWithoutReviews(Long memberId) {
        List<Payment> payments = paymentRepository.findAllPaymentsByMemberIdWithNonNullService(memberId);

        Set<Long> paymentIdsWithReviews = reviewRepository.findByMemberIdNotNull(memberId)
                .stream()
                .map(review -> review.getPayment().getId())
                .collect(Collectors.toSet());

        return payments.stream()
                .filter(payment -> !paymentIdsWithReviews.contains(payment.getId()))
                .map(this::entityToDto)
                .collect(Collectors.toList());
    }



    public PaymentDTO findById(Long id, Long memberNo) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Payment not found"));

        if (!payment.getMember().getId().equals(memberNo)) {
            throw new AccessDeniedException("You don't have permission to view this payment");
        }

        return entityToDto(payment);
    }

    private Payment dtoToEntity(PaymentDTO dto) {
        Payment.PaymentBuilder paymentBuilder = Payment.builder()
                .id(dto.getPayNo())
                .payType(dto.getPayType())
                .payPrice(dto.getPayPrice())
                .payBalanceSnap(dto.getPayBalanceSnap())
                .transitBalanceSnap(dto.getTransitBalanceSnap())
                .payDate(dto.getPayDate())
                .status(dto.getStatus())
                .serviceName(dto.getServiceName())
                .userCard(UserCard.builder().id(dto.getUserCardNo()).build())
                .member(Member.builder().id(dto.getMemberNo()).build());

        if(dto.getServiceNo() != null) {
            Services service = servicesRepository.findById(dto.getServiceNo())
                    .orElseThrow(() -> new RuntimeException("Service not found"));
            paymentBuilder.services(service);
        }

        return paymentBuilder.build();
    }

    private PaymentDTO entityToDto(Payment payment) {

        PaymentDTO.PaymentDTOBuilder dtoBuilder = PaymentDTO.builder()
                .payNo(payment.getId())
                .payType(payment.getPayType())
                .payPrice(payment.getPayPrice())
                .payBalanceSnap(payment.getPayBalanceSnap())
                .transitBalanceSnap(payment.getTransitBalanceSnap())
                .payDate(payment.getPayDate())
                .status(payment.getStatus())
                .serviceName(payment.getServiceName())
                .userCardNo(payment.getUserCard().getId())
                .memberNo(payment.getMember().getId());

        if (payment.getServices() != null) {
            dtoBuilder.serviceNo(payment.getServices().getId());
        }

        return dtoBuilder.build();

    }


}