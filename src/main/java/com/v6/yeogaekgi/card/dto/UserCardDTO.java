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
    private Long userCardNo;
    private Timestamp expiryDate;
    private int payBalance;
    private int transitBalance;
    private int starred;
    private int status;
    // Card
    private Long cardNo;
    private String design;
    private String area;
    private String cardName;
    // Member
    private Long memberNo;

    public void updateStarred(int starred) {
        this.starred = starred;
    }

    public void updateStatus(int status) {
        this.status = status;
    }
}
