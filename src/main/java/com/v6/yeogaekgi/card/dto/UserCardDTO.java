package com.v6.yeogaekgi.card.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.joda.time.DateTime;

import java.sql.Timestamp;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserCardDTO {
    private Long userCardId;
    private Timestamp expiryDate;
    private int payBalance;
    private int transitBalance;
    private int starred;
    private int status;
    // Card
    private Long cardId;
    // Member
    private Long memberId;
}
