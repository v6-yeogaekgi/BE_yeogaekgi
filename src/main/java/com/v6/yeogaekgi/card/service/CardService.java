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

    public List<String> getCardAreas() {
        List<String> result = cardRepository.findDistinctArea();
        return result;
    }

    public Card dtoToEntity(CardDTO cardDTO) {
        Card card = Card.builder()
                .no(cardDTO.getCardNo())
                .design(cardDTO.getDesign())
                .cardName(cardDTO.getCardName())
                .area(cardDTO.getArea())
                .build();
        return card;
    }

    public CardDTO entityToDto(Card card) {
        CardDTO cardDTO = CardDTO.builder()
                .cardNo(card.getNo())
                .design(card.getDesign())
                .cardName(card.getCardName())
                .area(card.getArea())
                .build();
        return cardDTO;
    }
}
