package com.v6.yeogaekgi.payTrack.service;

import com.v6.yeogaekgi.card.entity.UserCard;
import com.v6.yeogaekgi.card.repository.UserCardRepository;
import com.v6.yeogaekgi.member.entity.Member;
import com.v6.yeogaekgi.payTrack.dto.TransactionDTO;
import com.v6.yeogaekgi.payTrack.entity.Transaction;
import com.v6.yeogaekgi.payTrack.repository.TransactionRepository;
import java.util.NoSuchElementException;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.sql.Timestamp;

@Service
@Log4j2
@AllArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;

    private final UserCardRepository userCardRepository;

    public TransactionDTO getTransactionByNo(Long tranNo) {
        TransactionDTO resultDTO = transactionRepository.findById(tranNo)
                .map(this::entityToDto)
                .orElseThrow(() -> new NoSuchElementException("거래내역을 찾을 수 없습니다"));

        return resultDTO;
    }

    @Transactional
    public void topupTransaction(TransactionDTO transactionDTO, Member member) {
        UserCard existedUserCard = userCardRepository.findById(transactionDTO.getUserCardNo())
                .orElseThrow(() -> new NoSuchElementException("카드를 찾을 수 없습니다"));

        int krw = transactionDTO.getKrwAmount().intValue();

        int afterPayBalance = existedUserCard.getPayBalance() + krw;

        log.info("충전 전 - 페이 잔액: {}",
                existedUserCard.getPayBalance());

        Transaction transaction = Transaction.builder()
                .tranType(1)
                // 잔액 충전
                .payBalanceSnap(afterPayBalance)
                .transitBalanceSnap(existedUserCard.getTransitBalance())
                .krwAmount(transactionDTO.getKrwAmount())
                //환율 API 적용
                .foreignAmount(transactionDTO.getForeignAmount())
                .currencyType(transactionDTO.getCurrencyType())
                .userCard(existedUserCard)
                .member(member)
                .build();

        Transaction savedTransaction = transactionRepository.save(transaction);

        if (savedTransaction == null) {
            throw new IllegalStateException("거래 저장에 실패했습니다");
        }

        // 유저 카드 잔액 업데이트
        existedUserCard.updatePayBalance(afterPayBalance);
        UserCard savedCard = userCardRepository.save(existedUserCard);

        if (savedCard == null) {
            throw new IllegalStateException("카드 잔액 수정에 실패했습니다");
        }

        log.info("충전 후 - 페이 잔액: {}",
                savedCard.getPayBalance());
    }

    @Transactional
    public void refundTransaction(TransactionDTO transactionDTO, Member member) {

        // ----------------------------------
        // 실제 서비스 시 외부 API 호출 로직
        if (!apiAccess(member.getBank(), member.getName(), member.getAccountNumber())) {
            throw new IllegalArgumentException("은행 API 접근에 실패했습니다");
        }
        // ----------------------------------

        // 0: USD | 1: JPY | 2: CNY | 3: KRW
        int currency_type = transactionDTO.getCurrencyType();

        UserCard userCard = userCardRepository.findById(transactionDTO.getUserCardNo())
                .orElseThrow(() -> new NoSuchElementException("카드를 찾을 수 없습니다"));

        if (userCard.getPayBalance() < 3000) {
            throw new IllegalArgumentException("환불을 위한 최소 잔액(3000원)이 부족합니다");
        }

        log.info("환급 전 - 페이 잔액: {}",
                userCard.getPayBalance());

        Transaction savedTransaction = transactionRepository.save(
                Transaction
                        .builder()
                        .tranType(2) // 환급 타입
                        .currencyType(currency_type)
                        .krwAmount(BigDecimal.valueOf(userCard.getPayBalance() - 3000)) // 수수료 3,000원
                        .foreignAmount(transactionDTO.getForeignAmount())
                        .tranDate(new Timestamp(System.currentTimeMillis()))
                        .payBalanceSnap(0)
                        .transitBalanceSnap(userCard.getTransitBalance())
                        .member(Member.builder()
                                .no(member.getNo())
                                .build())
                        .userCard(userCard)
                        .build()
        );

        // userCard pay 잔액 0원으로 변경
        if (savedTransaction == null) {
            throw new IllegalStateException("환불 거래 저장에 실패했습니다");
        }

        userCard.updatePayBalance(0);
        UserCard savedCard = userCardRepository.save(userCard);

        if (savedCard == null) {
            throw new IllegalStateException("카드 잔액 수정에 실패했습니다");
        }

        log.info("환급 후 - 페이 잔액: {}",
                savedCard.getPayBalance());
    }

    @Transactional
    public void conversionAmount(TransactionDTO transactionDTO, Member member) {
        int transferAmount = transactionDTO.getKrwAmount().intValue();

        if (transferAmount == 0) {
            throw new IllegalArgumentException("전환 금액은 0원일 수 없습니다");
        }

        UserCard userCard = userCardRepository.findById(transactionDTO.getUserCardNo())
                .orElseThrow(() -> new NoSuchElementException("카드를 찾을 수 없습니다"));

        log.info("전환 전 - 페이 잔액: {}, 교통 잔액: {}",
                userCard.getPayBalance(), userCard.getTransitBalance());

        if (!updateCardBalance(userCard, transferAmount, transactionDTO.getTransferType())) {
            throw new IllegalArgumentException("잔액이 부족합니다");
        }

        UserCard savedUserCard = userCardRepository.save(userCard);

        if (!saveTransaction(savedUserCard, member, transferAmount, transactionDTO.getTransferType())) {
            throw new IllegalStateException("전환 거래 저장에 실패했습니다");
        }

        log.info("전환 후 - 페이 잔액: {}, 교통 잔액: {}",
                savedUserCard.getPayBalance(), savedUserCard.getTransitBalance());
    }

    private boolean updateCardBalance(UserCard userCard, int transferAmount, int transferType) {
        int payBalance = userCard.getPayBalance();
        int transitBalance = userCard.getTransitBalance();

        if (transferType == 0) {
            if (payBalance - transferAmount < 0) {
                return false;
            }
            userCard.updatePayBalance(payBalance - transferAmount);
            userCard.updateTransitBalance(transitBalance + transferAmount);
        } else {
            if (transitBalance - transferAmount < 0) {
                return false;
            }
            userCard.updatePayBalance(payBalance + transferAmount);
            userCard.updateTransitBalance(transitBalance - transferAmount);
        }

        return true;
    }

    private boolean saveTransaction(UserCard userCard, Member member, int transferAmount, int transferType) {
        Transaction build = Transaction.builder()
                .tranType(0)
                .transferType((byte) transferType)
                .tranDate(new Timestamp(System.currentTimeMillis()))
                .payBalanceSnap(userCard.getPayBalance())
                .transitBalanceSnap(userCard.getTransitBalance())
                .krwAmount(BigDecimal.valueOf(transferAmount))
                .currencyType(3) //KRW
                .userCard(userCard)
                .member(member)
                .build();

        Transaction savedTransaction = transactionRepository.save(build);
        return savedTransaction != null;
    }

    private boolean apiAccess(String bank, String name, String accountNumber) {
        return true;
    }

    public Transaction dtoToEntity(TransactionDTO transactionDTO) {
        return Transaction.builder()
                .no(transactionDTO.getTranNo())
                .tranType(transactionDTO.getTranType())
                .tranDate(transactionDTO.getTranDate())
                .transferType(transactionDTO.getTransferType())
                .payBalanceSnap(transactionDTO.getPayBalanceSnap())
                .transitBalanceSnap(transactionDTO.getTransitBalanceSnap())
                .krwAmount(transactionDTO.getKrwAmount())
                .foreignAmount(transactionDTO.getForeignAmount())
                .currencyType(transactionDTO.getCurrencyType())
                .userCard(UserCard.builder().no(transactionDTO.getUserCardNo()).build())
                .member(Member.builder().no(transactionDTO.getMemberNo()).build())
                .build();
    }

    public TransactionDTO entityToDto(Transaction transaction) {
        return TransactionDTO.builder()
                .tranNo(transaction.getNo())
                .tranType(transaction.getTranType())
                .tranDate(transaction.getTranDate())
                .transferType(transaction.getTransferType())
                .payBalanceSnap(transaction.getPayBalanceSnap())
                .transitBalanceSnap(transaction.getTransitBalanceSnap())
                .krwAmount(transaction.getKrwAmount())
                .foreignAmount(transaction.getForeignAmount())
                .currencyType(transaction.getCurrencyType())
                .userCardNo(transaction.getUserCard().getNo())
                .memberNo(transaction.getMember().getNo())
                .build();
    }
}
