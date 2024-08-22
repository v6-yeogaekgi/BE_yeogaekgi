package com.v6.yeogaekgi.payTrack.service;

import com.v6.yeogaekgi.card.entity.UserCard;
import com.v6.yeogaekgi.card.repository.UserCardRepository;
import com.v6.yeogaekgi.member.entity.Country;
import com.v6.yeogaekgi.member.entity.Member;
import com.v6.yeogaekgi.member.repository.MemberRepository;
import com.v6.yeogaekgi.payTrack.dto.TransactionDTO;
import com.v6.yeogaekgi.payTrack.entity.Transaction;
import com.v6.yeogaekgi.payTrack.repository.TransactionRepository;
import com.v6.yeogaekgi.security.MemberDetailsImpl;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Optional;

@Service
@Log4j2
@AllArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;

    private final UserCardRepository userCardRepository;

    public TransactionDTO getTransactionById(Long tranId) {
        TransactionDTO resultDTO = transactionRepository.findById(tranId)
                .map(this::entityToDto)
                .orElse(null);

        return resultDTO;
    }

    @Transactional
    public boolean refundTransaction(TransactionDTO transactionDTO, Member member) {
        String bank = member.getBank();
        String name = member.getName();
        String accountNumber = member.getAccountNumber();

        // ----------------------------------
        // 실제 서비스 시 외부 API 호출 로직
        boolean access = apiAccess(bank, name, accountNumber);
        // ----------------------------------

//        String code = member.getCountry().getCode();

        // 0: USD | 1: JPY | 2: CNY | 3: KRW
//        int currency_type = "US".equals(code)?0: ("JP".equals(code)?1: ("CN".equals(code)?2:3));
        int currency_type = transactionDTO.getCurrencyType();

        if(access){
            Optional<UserCard> optionalUserCard = userCardRepository.findById(transactionDTO.getUserCardNo());
            if(optionalUserCard.isPresent()){
                UserCard userCard = optionalUserCard.get();

                if(userCard.getPayBalance() < 3000){
                    return false;
                }

                Transaction savedTransaction = transactionRepository.save(
                        Transaction
                                .builder()
                                .tranType(2) // 환급 타입
                                .currencyType(currency_type)
                                .krwAmount(BigDecimal.valueOf(userCard.getPayBalance() - 3000)) // 수수료 3,000원
                                .foreignAmount(BigDecimal.valueOf((userCard.getPayBalance() - 3000) / 100)) // todo 번역 API 연동 시 userCard.getPayBalance 를 currency_type 에 따라 환율 적용
                                .tranDate(new Timestamp(System.currentTimeMillis()))
                                .payBalanceSnap(0)
                                .transitBalanceSnap(userCard.getTransitBalance())
                                .member(Member.builder()
                                        .id(member.getId())
                                        .build())
                                .userCard(userCard)
                                .build()
                );

                // userCard pay 잔액 0원으로 변경
                if(savedTransaction!=null){
                    userCard.updatePayBalance(0);
                    UserCard savedUserCard = userCardRepository.save(userCard);
                    if(savedUserCard !=null){
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public boolean apiAccess(String bank, String name, String accountNumber){
        return true;
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
