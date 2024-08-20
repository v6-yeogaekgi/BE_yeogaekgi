package com.v6.yeogaekgi.payTrack.service;

import com.v6.yeogaekgi.card.entity.UserCard;
import com.v6.yeogaekgi.member.entity.Member;
import com.v6.yeogaekgi.payTrack.dto.TransactionDTO;
import com.v6.yeogaekgi.payTrack.entity.Transaction;
import com.v6.yeogaekgi.payTrack.repository.TransactionRepository;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Log4j2
@AllArgsConstructor

public class TransactionService {
    private final TransactionRepository transactionRepository;

    public TransactionDTO getTransactionById(Long tranId) {
        TransactionDTO resultDTO = transactionRepository.findById(tranId)
                .map(this::entityToDto)
                .orElse(null);

        return resultDTO;
    }

    public Transaction dtoToEntity(TransactionDTO transactionDTO) {
        Transaction transaction = Transaction.builder()
                .id(transactionDTO.getTranNo())
                .tranType(transactionDTO.getTranType())
                .tranDate(transactionDTO.getTranDate())
                .transferType(transactionDTO.getTransferType())
                .payBalanceSnap(transactionDTO.getPayBalanceSnap())
                .transitBalanceSnap(transactionDTO.getTransitBalanceSnap())
                .krwAmount(transactionDTO.getKrwAmount())
                .foreignAmount(transactionDTO.getForeignAmount())
                .currencyType(transactionDTO.getCurrencyType())
                .userCard(UserCard.builder().id(transactionDTO.getUserCardNo()).build())
                .member(Member.builder().id(transactionDTO.getMemberNo()).build())
                .build();
        return transaction;
    }

    public TransactionDTO entityToDto(Transaction transaction) {
        TransactionDTO transactionDTO = TransactionDTO.builder()
                .tranNo(transaction.getId())
                .tranType(transaction.getTranType())
                .tranDate(transaction.getTranDate())
                .transferType(transaction.getTransferType())
                .payBalanceSnap(transaction.getPayBalanceSnap())
                .transitBalanceSnap(transaction.getTransitBalanceSnap())
                .krwAmount(transaction.getKrwAmount())
                .foreignAmount(transaction.getForeignAmount())
                .currencyType(transaction.getCurrencyType())
                .userCardNo(transaction.getUserCard().getId())
                .memberNo(transaction.getMember().getId())
                .build();
        return transactionDTO;
    }
}
