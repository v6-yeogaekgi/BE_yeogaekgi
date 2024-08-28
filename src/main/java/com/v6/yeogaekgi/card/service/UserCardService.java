package com.v6.yeogaekgi.card.service;

import com.v6.yeogaekgi.card.dto.UserCardDTO;
import com.v6.yeogaekgi.card.entity.Card;
import com.v6.yeogaekgi.card.entity.UserCard;
import com.v6.yeogaekgi.card.repository.UserCardRepository;
import com.v6.yeogaekgi.member.entity.Member;
import io.jsonwebtoken.io.IOException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Log4j2
@RequiredArgsConstructor

public class UserCardService {
    private final UserCardRepository userCardRepository;

    public List<UserCardDTO> getUserCardByUserId(Long userId) {
        List<UserCard> result = userCardRepository.findByMember_Id(userId);
        return result.stream().map(UserCard -> entityToDto(UserCard)).collect(Collectors.toList());
    }

    public UserCardDTO getUserCardByUserCardId(Long userCardId) {
        return userCardRepository.findById(userCardId)
                .map(this::entityToDto)
                .orElseThrow(() -> new EntityNotFoundException("UserCard not found with id: " + userCardId));
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

    @Transactional
    public boolean changesUserCardStarred(UserCardDTO userCardDTO){
        // parameter userCardDTO ->
        // userCardNo = 주카드 타겟
        // memberNo = 주카드 갱신 사용자

        UserCardDTO prevUserCard = getUserCardByUserCardId(userCardDTO.getUserCardId());
        UserCardDTO nextUserCard = null;

        try{
            List<UserCardDTO> all = getAllByMemberId(prevUserCard.getMemberId());
            for (UserCardDTO cardDTO : all) {
                if(cardDTO.getStatus() == 1){
                    if(cardDTO.getStarred()==1){
                        cardDTO.updateStarred(0);
                        userCardRepository.save(dtoToEntity(cardDTO));
                    }
                    if(cardDTO.getUserCardId().equals(userCardDTO.getUserCardId())){ // use userCardNo
                        cardDTO.updateStarred(1);;
                        UserCard saved = userCardRepository.save(dtoToEntity(cardDTO));
                        nextUserCard = entityToDto(saved);
                    }
                }
            }
            if (nextUserCard != null && nextUserCard.getStarred() != prevUserCard.getStarred()) return true;

            return false;
        }catch (Exception e){
            throw new RuntimeException("error", e);
        }
    }

    public boolean deleteUserCardStarred(UserCardDTO userCardDTO){
        int prevStatus = userCardDTO.getStarred();
        try {
            UserCardDTO userCard = getUserCardByUserCardId(userCardDTO.getUserCardId());
            userCard.updateStarred(0);
            UserCard save = userCardRepository.save(dtoToEntity(userCard));
            if(save.getStarred() != prevStatus) return true;
        }catch (Exception e){
            throw new RuntimeException("error", e);
        }
        return false;
    }

    public boolean deleteUserCard(UserCardDTO userCardDTO){
        try {
            UserCardDTO userCard = getUserCardByUserCardId(userCardDTO.getUserCardId());
            userCard.updateStatus(2);
            // 삭제 대상 카드가 주카드일때 주카드 상태 해제 이후 save
            if(userCard.getStarred()==1) userCard.setStarred(0);
            UserCard save = userCardRepository.save(dtoToEntity(userCard));
            if(save.getStatus() == 2) return true;
        }catch (Exception e){
            throw new RuntimeException("error", e);
        }
        return false;
    }

    public List<UserCardDTO> getAllByMemberId(Long memberId) {
        List<UserCard> find = userCardRepository.findByMember_Id(memberId);
        return find.stream().map(this::entityToDto).collect(Collectors.toList());
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
                .starred(userCardDTO.getStarred())
                .status(userCardDTO.getStatus())
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
