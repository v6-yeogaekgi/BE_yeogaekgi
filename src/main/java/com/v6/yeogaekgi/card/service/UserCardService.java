package com.v6.yeogaekgi.card.service;

import com.v6.yeogaekgi.card.dto.UserCardDTO;
import com.v6.yeogaekgi.card.entity.Card;
import com.v6.yeogaekgi.card.entity.UserCard;
import com.v6.yeogaekgi.card.repository.UserCardRepository;
import com.v6.yeogaekgi.member.entity.Member;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@Log4j2
@RequiredArgsConstructor

public class UserCardService {
    private final UserCardRepository userCardRepository;

    public List<UserCardDTO> getUserCardByUserNo(Long userNo) {
        List<UserCard> result = userCardRepository.findByMemberNo(userNo);
        if (result.isEmpty()) {
            throw new NoSuchElementException("사용자의 카드를 찾을 수 없습니다.");
        }
        return result.stream()
                .map(this::entityToDto)
                .collect(Collectors.toList());
    }

    public UserCardDTO getUserCardByUserCardNo(Long userCardNo, Long memberNo) {
        if (!isUserCardOwner(memberNo, userCardNo)) {
            throw new AccessDeniedException("해당 카드에 대한 접근 권한이 없습니다.");
        }
        return userCardRepository.findById(userCardNo)
                .map(this::entityToDto)
                .orElseThrow(() -> new EntityNotFoundException("카드를 찾을 수 없습니다. ID: " + userCardNo));
    }

//    @Transactional
//    public void updateBalance(UserCardDTO userCardDTO) {
//        UserCard userCard = dtoToEntity(userCardDTO);
//        userCardRepository.save(userCard);
//    }

    @Transactional
    public boolean changeUserCardStarred(UserCardDTO userCardDTO, Long memberNo){
        if (!isUserCardOwner(memberNo, userCardDTO.getUserCardNo())) {
            throw new AccessDeniedException("해당 카드에 대한 접근 권한이 없습니다.");
        }

        List<UserCardDTO> allCards = getAllByMemberNo(memberNo);

        // 현재 starred 상태인 카드들의 starred를 0으로 변경
        allCards.stream()
                .filter(card -> card.getStatus() == 1 && card.getStarred() == 1)
                .forEach(card -> {
                    card.updateStarred(0);
                    userCardRepository.save(dtoToEntity(card));
                });

        // 대상 카드를 starred로 변경
        return allCards.stream()
                .filter(card -> card.getStatus() == 1 && card.getUserCardNo().equals(userCardDTO.getUserCardNo()))
                .findFirst()
                .map(card -> {
                    card.updateStarred(1);
                    userCardRepository.save(dtoToEntity(card));
                    return true;
                })
                .orElseThrow(() -> new NoSuchElementException("대상 카드를 찾을 수 없습니다."));
    }

    public boolean deleteUserCardStarred(UserCardDTO userCardDTO, Long memberNo){
        if (!isUserCardOwner(memberNo, userCardDTO.getUserCardNo())) {
            throw new AccessDeniedException("해당 카드에 대한 접근 권한이 없습니다.");
        }

        UserCardDTO userCard = getUserCardByUserCardNo(userCardDTO.getUserCardNo(), memberNo);
        if (userCard.getStarred() == 0) {
            throw new HttpMessageNotWritableException("이미 주카드가 아닙니다.");
        }

        userCard.updateStarred(0);
        UserCard savedCard = userCardRepository.save(dtoToEntity(userCard));
        if(savedCard.getStarred() != 0) {
            throw new RuntimeException("주카드 해제에 실패했습니다.");
        }

        return true;
    }

    public boolean deleteUserCard(UserCardDTO userCardDTO, Long memberNo){
        if (!isUserCardOwner(memberNo, userCardDTO.getUserCardNo())) {
            throw new AccessDeniedException("해당 카드에 대한 접근 권한이 없습니다.");
        }

        UserCardDTO userCard = getUserCardByUserCardNo(userCardDTO.getUserCardNo(), memberNo);
        if (userCard.getStatus() == 2) {
            throw new HttpMessageNotWritableException("이미 삭제된 카드입니다.");
        }
        userCard.updateStatus(2);
        // 주카드였다면 해제
        if (userCard.getStarred()==1) userCard.setStarred(0);

        UserCard savedCard = userCardRepository.save(dtoToEntity(userCard));
        if (savedCard.getStatus() != 2) {
            throw new RuntimeException("카드 삭제 처리에 실패했습니다.");
        }

        return true;
    }

    public List<UserCardDTO> getAllByMemberNo(Long memberNo) {
        List<UserCard> find = userCardRepository.findByMemberNo(memberNo);
        return find.stream().map(this::entityToDto).collect(Collectors.toList());
    }

    public List<UserCardDTO> getHomeCardByMemberAndArea(Long memberNo, String area) {
        if (area == null) {
            throw new IllegalArgumentException("지역 정보가 없습니다.");
        }

        List<UserCard> userCards = userCardRepository.findByMemberNo(memberNo);
        List<UserCardDTO> result = userCards.stream()
                .filter(userCard -> userCard.getCard().getArea() != null)
                .filter(userCard -> userCard.getCard().getArea().equals(area))
                .map(this::entityToDto)
                .collect(Collectors.toList());

        // TODO - 빈 리스트라면 프론트에서 처리하기
        return result;
    }

    private boolean isUserCardOwner(Long memberNo, Long userCardNo) {
        return userCardRepository.existsByNoAndMemberNo(userCardNo, memberNo);
    }

    public UserCard dtoToEntity(UserCardDTO userCardDTO) {
        UserCard userCard = UserCard.builder()
                .no(userCardDTO.getUserCardNo())
                .expDate(userCardDTO.getExpiryDate())
                .payBalance(userCardDTO.getPayBalance())
                .transitBalance(userCardDTO.getTransitBalance())
                .starred(userCardDTO.getStarred())
                .status(userCardDTO.getStatus())
                .card(Card.builder().no(userCardDTO.getCardNo()).build())
                .member(Member.builder().no(userCardDTO.getMemberNo()).build())
                .build();
        return userCard;
    };

    public UserCardDTO entityToDto(UserCard userCard) {
        UserCardDTO userCardDTO = UserCardDTO.builder()
                .userCardNo(userCard.getNo())
                .expiryDate(userCard.getExpDate())
                .payBalance(userCard.getPayBalance())
                .transitBalance(userCard.getTransitBalance())
                .starred(userCard.getStarred())
                .status(userCard.getStatus())
                .cardNo(userCard.getCard().getNo())
                .memberNo(userCard.getMember().getNo())
                .design(userCard.getCard().getDesign())
                .area(userCard.getCard().getArea())
                .cardName(userCard.getCard().getCardName())
                .build();
        return userCardDTO;
    };
}
