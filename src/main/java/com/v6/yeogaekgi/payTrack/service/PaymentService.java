package com.v6.yeogaekgi.payTrack.service;

import com.v6.yeogaekgi.card.entity.UserCard;
import com.v6.yeogaekgi.card.repository.UserCardRepository;
import com.v6.yeogaekgi.member.entity.Member;
import com.v6.yeogaekgi.payTrack.dto.PaymentDTO;
import com.v6.yeogaekgi.payTrack.entity.Payment;
import com.v6.yeogaekgi.payTrack.repository.PaymentRepository;
import com.v6.yeogaekgi.review.repository.ReviewRepository;
import com.v6.yeogaekgi.services.entity.Services;
import com.v6.yeogaekgi.services.repository.ServicesRepository;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public void insertPayment(PaymentDTO paymentDTO) {
        UserCard prevUserCard = userCardRepository.findById(paymentDTO.getUserCardNo())
                .orElseThrow(() -> new NoSuchElementException("카드를 찾을 수 없습니다"));
        log.info("저장할 결제내역  - 결제 타입 {} - 결제 금액 {} - 결제 날짜 {} - 결제 상태 {} - 서비스 번호 {} - 사용자 카드 번호 {} - 사용자 번호 {}",
                paymentDTO.getPayType(),
                paymentDTO.getPayPrice(),
                paymentDTO.getPayDate(),
                paymentDTO.getStatus(),
                paymentDTO.getServiceNo(),
                paymentDTO.getUserCardNo(),
                paymentDTO.getMemberNo()
                );


        int payBalance = prevUserCard.getPayBalance();
        int transitBalance = prevUserCard.getTransitBalance();

        if (paymentDTO.getPayType() == 1) {
            transitBalance -= paymentDTO.getPayPrice();
        } else {
            payBalance -= paymentDTO.getPayPrice();
        }

        prevUserCard.updatePayBalance(payBalance);
        prevUserCard.updateTransitBalance(transitBalance);
        UserCard savedCard = userCardRepository.save(prevUserCard);

        if (savedCard == null) {
            throw new IllegalStateException("카드 잔액 수정에 실패했습니다");
        }

        if (paymentDTO.getServiceNo() != null) {
            Optional<Services> service = servicesRepository.findById(paymentDTO.getServiceNo());
            if (service.isPresent()) {
                paymentDTO.setServiceName(service.get().getName());
            } else {
                log.warn("Service not found for serviceNo: {}", paymentDTO.getServiceNo());
                paymentDTO.setServiceName("Unknown Service");
            }
        }

        Payment savedPayment = paymentRepository.save(dtoToEntity(paymentDTO));

        if(savedPayment == null) {
            throw new IllegalStateException("결제내역 저장에 실패했습니다");
        }

        log.info("결제내역 저장 완료");
        log.info("저장된 결제내역  - 결제 번호 {} - 결제 타입 {} - 결제 금액 {}- 결제 날짜 {} - 결제 상태 {} - 서비스 이름 {} - 사용자 카드 번호 {} - 사용자 번호 {}",
                paymentDTO.getPayNo(),
                paymentDTO.getPayType(),
                paymentDTO.getPayPrice(),
                paymentDTO.getPayDate(),
                paymentDTO.getStatus(),
                paymentDTO.getServiceName(),
                paymentDTO.getUserCardNo(),
                paymentDTO.getMemberNo()
        );
        log.info("결제내역 저장 후 - 페이 잔액: {}, 교통 잔액: {}",
                savedCard.getPayBalance(), savedCard.getTransitBalance());
    }

    public List<PaymentDTO> findPaymentsWithoutReviews(Long memberNo) {
        List<Payment> payments = paymentRepository.findAllPaymentsByMemberNoWithNonNullService(memberNo);

        Set<Long> paymentNosWithReviews = reviewRepository.findByMemberNoNotNull(memberNo)
                .stream()
                .map(review -> review.getPayment().getNo())
                .collect(Collectors.toSet());

        return payments.stream()
                .filter(payment -> !paymentNosWithReviews.contains(payment.getNo()))
                .map(this::entityToDto)
                .collect(Collectors.toList());
    }


    public PaymentDTO findByNo(Long no, Long memberNo) {
        Payment payment = paymentRepository.findById(no)
                .orElseThrow(() -> new NoSuchElementException("결제내역을 찾을 수 없습니다"));

        if (!payment.getMember().getNo().equals(memberNo)) {
            throw new AccessDeniedException("You don't have permission to view this payment");
        }

        return entityToDto(payment);
    }

    private Payment dtoToEntity(PaymentDTO dto) {
        Payment.PaymentBuilder paymentBuilder = Payment.builder()
                .no(dto.getPayNo())
                .payType(dto.getPayType())
                .payPrice(dto.getPayPrice())
                .payBalanceSnap(dto.getPayBalanceSnap())
                .transitBalanceSnap(dto.getTransitBalanceSnap())
                .payDate(dto.getPayDate())
                .status(dto.getStatus())
                .serviceName(dto.getServiceName())
                .userCard(UserCard.builder().no(dto.getUserCardNo()).build())
                .member(Member.builder().no(dto.getMemberNo()).build());

        if (dto.getServiceNo() != null) {
            Services service = servicesRepository.findById(dto.getServiceNo())
                    .orElseThrow(() -> new NoSuchElementException("서비스를 찾을 수 없습니다"));
            paymentBuilder.services(service);
        }

        return paymentBuilder.build();
    }

    private PaymentDTO entityToDto(Payment payment) {

        PaymentDTO.PaymentDTOBuilder dtoBuilder = PaymentDTO.builder()
                .payNo(payment.getNo())
                .payType(payment.getPayType())
                .payPrice(payment.getPayPrice())
                .payBalanceSnap(payment.getPayBalanceSnap())
                .transitBalanceSnap(payment.getTransitBalanceSnap())
                .payDate(payment.getPayDate())
                .status(payment.getStatus())
                .serviceName(payment.getServiceName())
                .userCardNo(payment.getUserCard().getNo())
                .memberNo(payment.getMember().getNo());

        if (payment.getServices() != null) {
            dtoBuilder.serviceNo(payment.getServices().getNo());
        }

        return dtoBuilder.build();

    }


}