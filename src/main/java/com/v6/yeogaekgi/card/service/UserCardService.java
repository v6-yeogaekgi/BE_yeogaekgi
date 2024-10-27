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
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Log4j2
@RequiredArgsConstructor

public class UserCardService {
    private final UserCardRepository userCardRepository;

    public List<UserCardDTO> getUserCardByUserNo(Long userNo) {
        List<UserCard> result = userCardRepository.findByMember(userNo);
        return result.stream().map(UserCard -> entityToDto(UserCard)).collect(Collectors.toList());
    }

    public UserCardDTO getUserCardByUserCardNo(Long userCardNo, Long memberNo) {
        if (!isUserCardOwner(memberNo, userCardNo)) {
            throw new AccessDeniedException("You don't have permission to access this card's data");
        }
        return userCardRepository.findById(userCardNo)
                .map(this::entityToDto)
                .orElseThrow(() -> new EntityNotFoundException("UserCard not found with id: " + userCardNo));
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
    public boolean changesUserCardStarred(UserCardDTO userCardDTO, Long memberNo){
        // parameter userCardDTO ->
        // userCardNo = 주카드 타겟
        // memberId = 주카드 갱신 사용자

        UserCardDTO prevUserCard = getUserCardByUserCardNo(userCardDTO.getUserCardNo(), memberNo);
        UserCardDTO nextUserCard = null;

        try{
            List<UserCardDTO> all = getAllByMemberNo(prevUserCard.getMemberNo());
            for (UserCardDTO cardDTO : all) {
                if(cardDTO.getStatus() == 1){
                    if(cardDTO.getStarred()==1){
                        cardDTO.updateStarred(0);
                        userCardRepository.save(dtoToEntity(cardDTO));
                    }
                    if(cardDTO.getUserCardNo().equals(userCardDTO.getUserCardNo())){ // use userCardNo
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

    public boolean deleteUserCardStarred(UserCardDTO userCardDTO, Long memberNo){

        try {
            UserCardDTO userCard = getUserCardByUserCardNo(userCardDTO.getUserCardNo(), memberNo);
            int prevStarred = userCard.getStarred();
            userCard.updateStarred(0);
            UserCard save = userCardRepository.save(dtoToEntity(userCard));

            if(save.getStarred() != prevStarred) return true;
        }catch (Exception e){
            throw new RuntimeException("error", e);
        }
        return false;
    }

    public boolean deleteUserCard(UserCardDTO userCardDTO, Long memberNo){
        try {
            UserCardDTO userCard = getUserCardByUserCardNo(userCardDTO.getUserCardNo(), memberNo);
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

    public List<UserCardDTO> getAllByMemberNo(Long memberNo) {
        List<UserCard> find = userCardRepository.findByMember(memberNo);
        return find.stream().map(this::entityToDto).collect(Collectors.toList());
    }

    public List<UserCardDTO> getHomeCardByMemberAndArea(Long memberNo, String area) {
        log.info("----getHomeCardByMemberAndArea----");

        List<UserCard> find = userCardRepository.findByMember(memberNo);
        List<UserCardDTO> result = new ArrayList<>();

        for(UserCard userCard : find) {
//            log.info(userCard.toString());
            String userCardArea = userCard.getCard().getArea();
            if(userCardArea != null && area != null){
                if(userCardArea.equals(area)) {
                    result.add(entityToDto(userCard));
                } else {
                    log.info(userCard.getCard().getArea());
                }
            } else {
                log.info("null error");
            }
        }
        log.info("result: " + result);
        return result;
    }

    private boolean isUserCardOwner(Long memberNo, Long userCardNo) {
        return userCardRepository.existsByNoAndMember(userCardNo, memberNo);
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
