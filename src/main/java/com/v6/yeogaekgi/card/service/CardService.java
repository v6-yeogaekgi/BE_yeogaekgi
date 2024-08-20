package com.v6.yeogaekgi.card.service;

import com.v6.yeogaekgi.card.dto.CardDTO;
import com.v6.yeogaekgi.card.entity.Card;
import com.v6.yeogaekgi.card.repository.CardRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Log4j2
@RequiredArgsConstructor

public class CardService {
    private final CardRepository cardRepository;

//    public List<CardDTO> getCardListByUserId(Long memberNo) {
//        List<Object[]> result = cardRepository.findCardByMemerNo(memberNo);
//        return result.stream().map(Card -> entityToDto(Card)).collect(Collectors.toList());
//    }

    public Card dtoToEntity(CardDTO cardDTO) {
        Card card = Card.builder()
                .id(cardDTO.getCardId())
                .design(cardDTO.getDesign())
                .cardName(cardDTO.getCardName())
                .area(cardDTO.getArea())
                .build();
        return card;
    }

    public CardDTO entityToDto(Card card) {
        CardDTO cardDTO = CardDTO.builder()
                .cardId(card.getId())
                .design(card.getDesign())
                .cardName(card.getCardName())
                .area(card.getArea())
                .build();
        return cardDTO;
    }
}
