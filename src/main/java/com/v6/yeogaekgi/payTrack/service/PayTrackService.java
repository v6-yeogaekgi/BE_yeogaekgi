package com.v6.yeogaekgi.payTrack.service;

import com.v6.yeogaekgi.card.repository.UserCardRepository;
import com.v6.yeogaekgi.payTrack.dto.PayTrackDTO;
import com.v6.yeogaekgi.payTrack.repository.PayTrackQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PayTrackService {

    private final PayTrackQueryRepository payTrackQueryRepository;

    private final UserCardRepository userCardRepository;

    public List<PayTrackDTO> findPayTrackByUserCardNo(long memberNo, long userCardNo, int year, int month) {

        if (!isUserCardOwner(memberNo, userCardNo)) {
            throw new AccessDeniedException("You don't have permission to access this card's data");
        }

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
                            .transitBalanceSnap(obj[7] != null ? ((Number) obj[7]).intValue() : null)
                            .payBalanceSnap(obj[8] != null ? ((Number) obj[8]).intValue() : null)
                            .tno(obj[9] != null ? ((Number) obj[9]).intValue() : null)
                            .tranType(obj[10] != null ? ((Number) obj[10]).intValue() : null)
                            .transferType(obj[11] != null ? ((Number) obj[11]).intValue() : null)
                            .krwAmount(obj[12] != null ? ((Number) obj[12]).intValue() : null)
                            .foreignAmount(obj[13] != null ? (BigDecimal) obj[13] : null)
                            .currencyType(obj[14] != null ? ((Number) obj[14]).intValue() : null)
                            .isPayment(isPayment)
                            .build();
                })
                .collect(Collectors.toList());

        return payTrackDTOList;
    }

    private boolean isUserCardOwner(Long memberNo, Long userCardNo) {
        return userCardRepository.existsByIdAndMemberNo(userCardNo, memberNo);
    }
}
