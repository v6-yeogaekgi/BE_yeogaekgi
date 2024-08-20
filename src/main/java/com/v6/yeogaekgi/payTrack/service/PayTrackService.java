package com.v6.yeogaekgi.payTrack.service;

import com.v6.yeogaekgi.payTrack.dto.PayTrackDTO;
import com.v6.yeogaekgi.payTrack.repository.PayTrackQueryRepository;
import com.v6.yeogaekgi.payTrack.repository.PaymentRepository;
import com.v6.yeogaekgi.payTrack.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PayTrackService {

    private final PaymentRepository paymentRepository;

    private final TransactionRepository transactionRepository;

    private final PayTrackQueryRepository payTrackQueryRepository;

    public List<PayTrackDTO> findPayTrackByUserCardNo(long userCardNo, int year, int month) {
        List<Object[]> payTrackByUserCardNo = payTrackQueryRepository.findPayTrackByUserCardNo(userCardNo, year, month);

        List<PayTrackDTO> payTrackDTOList = payTrackByUserCardNo.stream()
                .map(obj -> {
                    boolean isPayment = obj[2] != null; // pay_no가 null이 아니면 결제 내역

                    return PayTrackDTO.builder()
                            .userCardNo(((Number) obj[0]).longValue())
                            .datetime((Timestamp) obj[1])
                            .pno(obj[2] != null ? ((Number) obj[2]).intValue() : null)
                            .payType(obj[3] != null ? ((Number) obj[3]).intValue() : null)
                            .payPrice(obj[4] != null ? ((Number) obj[4]).intValue() : null)
                            .status(obj[5] != null ? ((Number) obj[5]).intValue() : null)
                            .serviceName((String) obj[6])
                            .payment_transit_balance_snap(obj[7] != null ? ((Number) obj[7]).intValue() : null)
                            .payment_pay_balance_snap(obj[8] != null ? ((Number) obj[8]).intValue() : null)
                            .tno(obj[9] != null ? ((Number) obj[9]).intValue() : null)
                            .tranType(obj[10] != null ? ((Number) obj[10]).intValue() : null)
                            .transfer_type(obj[11] != null ? ((Number) obj[11]).intValue() : null)
                            .krw_amount(obj[12] != null ? ((Number) obj[12]).intValue() : null)
                            .foreign_amount(obj[13] != null ? ((Number) obj[13]).intValue() : null)
                            .transaction_transit_balance_snap(obj[14] != null ? ((Number) obj[14]).intValue() : null)
                            .transaction_pay_balance_snap(obj[15] != null ? ((Number) obj[15]).intValue() : null)
                            .isPayment(isPayment)
                            .build();
                })
                .collect(Collectors.toList());

        return payTrackDTOList;
    }
}