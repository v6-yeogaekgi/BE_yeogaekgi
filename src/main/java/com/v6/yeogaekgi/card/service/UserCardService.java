package com.v6.yeogaekgi.card.service;

import com.v6.yeogaekgi.card.dto.UserCardDTO;
import com.v6.yeogaekgi.card.entity.Card;
import com.v6.yeogaekgi.card.entity.UserCard;
import com.v6.yeogaekgi.card.repository.CardRepository;
import com.v6.yeogaekgi.card.repository.UserCardRepository;
import com.v6.yeogaekgi.member.entity.Member;
import io.jsonwebtoken.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.awt.geom.Area;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Log4j2
@RequiredArgsConstructor

public class UserCardService {
    private final UserCardRepository userCardRepository;

//    public UserCardDTO getUserCardById(UserCardDTO userCardDTO) {
//        UserCard uc = userCardRepository.findById(userCardDTO.getUserCardId()).orElse(null);
//        return entityToDto(uc);
//    }

    public List<UserCardDTO> getUserCardByUserId(Long userId) {
        List<UserCard> result = userCardRepository.findByMember_Id(userId);
        return result.stream().map(UserCard -> entityToDto(UserCard)).collect(Collectors.toList());
    }

    public UserCardDTO getUserCardByCardId(Long cardId) {
        Optional<UserCard> byId = userCardRepository.findById(cardId);
        return byId.map(this::entityToDto).orElse(null);
    }

    @Transactional
    public void updateBalance(UserCardDTO userCardDTO) {
        try {
            UserCard userCard = dtoToEntity(userCardDTO);
            userCardRepository.save(userCard);
        } catch (IOException e) {
            throw new RuntimeException("error", e);
        }
    }

    public List<UserCardDTO> getAll(UserCardDTO userCardDTO) {
        log.info("==================");
        log.info("userCardDTO memberNo: " + userCardDTO.getMemberId());
        ArrayList<UserCard> result = new ArrayList<>();
        List<UserCard> find = userCardRepository.findByMember_Id(userCardDTO.getMemberId());
        for(UserCard uc : find) {
            log.info(uc.toString());
            log.info(uc.getCard().toString());
            log.info(uc.getCard().getArea());
            if(uc.getCard().getArea().equals(userCardDTO.getArea())) {
                result.add(uc);
            }
        }
        return result.stream().map(UserCard -> entityToDto(UserCard)).collect(Collectors.toList());
    }

    public UserCard dtoToEntity(UserCardDTO userCardDTO) {
        UserCard userCard = UserCard.builder()
                .id(userCardDTO.getUserCardId())
                .expDate(userCardDTO.getExpiryDate())
                .payBalance(userCardDTO.getPayBalance())
                .transitBalance(userCardDTO.getTransitBalance())
                .card(Card.builder().id(userCardDTO.getCardId()).build())
                .member(Member.builder().id(userCardDTO.getMemberId()).build())
                .build();
        return userCard;
    };

    public UserCardDTO entityToDto(UserCard userCard) {
        UserCardDTO userCardDTO = UserCardDTO.builder()
                .userCardId(userCard.getId())
                .expiryDate(userCard.getExpDate())
                .payBalance(userCard.getPayBalance())
                .transitBalance(userCard.getTransitBalance())
                .starred(userCard.getStarred())
                .status(userCard.getStatus())
                .cardId(userCard.getCard().getId())
                .memberId(userCard.getMember().getId())
                .design(userCard.getCard().getDesign())
                .area(userCard.getCard().getArea())
                .cardName(userCard.getCard().getCardName())
                .build();
        return userCardDTO;
    };
}
