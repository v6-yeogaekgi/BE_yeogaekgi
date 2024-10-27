package com.v6.yeogaekgi.payTrack.service;

import com.v6.yeogaekgi.card.entity.UserCard;
import com.v6.yeogaekgi.card.repository.UserCardRepository;
import com.v6.yeogaekgi.member.entity.Member;
import com.v6.yeogaekgi.payTrack.dto.TransactionDTO;
import com.v6.yeogaekgi.payTrack.entity.Transaction;
import com.v6.yeogaekgi.payTrack.repository.TransactionRepository;
import io.jsonwebtoken.io.IOException;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
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
    public boolean topupTransaction(TransactionDTO transactionDTO, Member member) {
        try {
            Optional<UserCard> optionalUserCard = userCardRepository.findById(transactionDTO.getUserCardNo());
            if (!optionalUserCard.isPresent()) {
                return false;
            }

            UserCard existedUserCard = optionalUserCard.get();

            int krw = transactionDTO.getKrwAmount().intValue();

            int afterPayBalance = existedUserCard.getPayBalance() + krw;

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

            if (savedTransaction != null) {
                // 유저 카드 잔액 업데이트
                existedUserCard.updatePayBalance(afterPayBalance);
                userCardRepository.save(existedUserCard);
                return true;
            }
        } catch (IOException e) {
            throw new RuntimeException("error", e);
        }
        return false;

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

        if (access) {
            Optional<UserCard> optionalUserCard = userCardRepository.findById(transactionDTO.getUserCardNo());
            if (optionalUserCard.isPresent()) {
                UserCard userCard = optionalUserCard.get();

                if (userCard.getPayBalance() < 3000) {
                    return false;
                }

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
                if (savedTransaction != null) {
                    userCard.updatePayBalance(0);
                    UserCard savedUserCard = userCardRepository.save(userCard);
                    if (savedUserCard != null) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Transactional
    public boolean conversionAmount(TransactionDTO transactionDTO, Member member) {
        int transferAmount = transactionDTO.getKrwAmount().intValue();

        if (transferAmount == 0) return false;

        int transferType = transactionDTO.getTransferType();

        Optional<UserCard> optionalUserCard = userCardRepository.findById(transactionDTO.getUserCardNo());

        if (optionalUserCard.isPresent()) {
            UserCard userCard = optionalUserCard.get();

            log.info("------------------------------------------------------------------------------------------");
            log.info("< Before > userCard.getPayBalance() :  " + userCard.getPayBalance());
            log.info("< Before > userCard.getTransitBalance() :  " + userCard.getTransitBalance());
            log.info("------------------------------------------------------------------------------------------");

            boolean updateStatus = updateCardBalance(userCard, transferAmount, transferType);
            UserCard savedUserCard = userCardRepository.save(userCard);

            if (!updateStatus){
                throw new RuntimeException("update balance failed");
            }

            log.info("------------------------------------------------------------------------------------------");
            log.info("< After > userCard.getPayBalance() :  " + savedUserCard.getPayBalance());
            log.info("< After > userCard.getTransitBalance() :  " + savedUserCard.getTransitBalance());
            log.info("------------------------------------------------------------------------------------------");

            if (updateStatus) {
                boolean saveResult = saveTransaction(savedUserCard, member, transferAmount, transferType);
                if(!saveResult){
                    throw new RuntimeException("save transaction failed");
                }
                return true;
            }
        }

        return false;
    }

    private boolean updateCardBalance(UserCard userCard, int transferAmount, int transferType) {
        int payBalance = userCard.getPayBalance();
        int transitBalance = userCard.getTransitBalance();

        if (transferType == 0) {
            if(payBalance - transferAmount < 0) return false;
            userCard.updatePayBalance(payBalance - transferAmount);
            userCard.updateTransitBalance(transitBalance + transferAmount);
        } else {
            if(transitBalance - transferAmount < 0) return false;
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
                .member(Member.builder().no(transactionDTO.getMemberNo()).build())
                .build();
    }

    public TransactionDTO entityToDto(Transaction transaction) {
        return TransactionDTO.builder()
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
                .memberNo(transaction.getMember().getNo())
                .build();
    }
}
