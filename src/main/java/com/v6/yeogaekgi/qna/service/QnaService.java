package com.v6.yeogaekgi.qna.service;

import com.v6.yeogaekgi.community.dto.SearchDTO;
import com.v6.yeogaekgi.community.entity.Post;
import com.v6.yeogaekgi.member.entity.Member;
import com.v6.yeogaekgi.qna.dto.QnaDTO;
import com.v6.yeogaekgi.qna.entity.Qna;
import com.v6.yeogaekgi.qna.repository.QnaRepository;
import com.v6.yeogaekgi.util.PageDTO.PageRequestDTO;
import com.v6.yeogaekgi.util.PageDTO.PageResultDTO;
import com.v6.yeogaekgi.util.S3.S3Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Log4j2
@RequiredArgsConstructor

public class QnaService {
    private QnaRepository repository;
    private S3Service s3Service;

    // Qna List
    public PageResultDTO<QnaDTO> getQnaList(PageRequestDTO pageRequestDTO, Member member) {
        if(member == null){
            throw new AccessDeniedException("QnA 리스트 접근 권한이 없습니다.");
        }
        Pageable pageable = PageRequest.of(pageRequestDTO.getPage(), 10, Sort.by(Sort.Direction.DESC, "no"));
        Slice<Qna> result = repository.findByMember_No(member.getNo(), pageable);
        if(result == null || result.getContent().isEmpty()){
            throw new NoSuchElementException("사용자의 QnA 리스트를 찾을 수 없습니다.");
        }
        List<QnaDTO> content = result.getContent().stream()
                .map(Qna -> entityToDto(Qna))
                .collect(Collectors.toList());
        PageResultDTO<QnaDTO> pageResultDTO = new PageResultDTO<>(content, pageable, result.hasNext());
        return pageResultDTO;

    }

    // Qna Detial
    public QnaDTO getQna(Long qnaNo, Member member) {
        if(member == null || ! repository.existsByNoAndMemberNo(qnaNo, member.getNo())){
            throw new AccessDeniedException("해당 QnA 글 접근 권한이 없습니다.");
        }
        Optional<Qna> qna = repository.findById(qnaNo);
        return entityToDto(qna.get());
    }

    // Qna Register
    public Long register(Qna qna) {
        if(qna.getMember() == null){
            throw new AccessDeniedException("QnA 글 작성 권한이 없습니다.");
        }
        Qna result = repository.save(qna);
        if(qna == null){
            throw new IllegalStateException("QnA 글 저장에 실패했습니다.");
        }
        return qna.getNo();
    }


    // Qna modify
    public Long modify(Qna qna) {
        if(qna.getMember() == null || ! repository.existsByNoAndMemberNo(qna.getNo(), qna.getMember().getNo())){
            throw new AccessDeniedException("해당 QnA 글 접근 권한이 없습니다.");
        }
        Qna result = repository.save(qna);
        if(result == null){
            throw new IllegalStateException("QnA 글 수정에 실패했습니다.");
        }
        return result.getNo();
    }

    // Qna remove
    public void remove(Long qnaNo, Member member) {
        if(member == null || ! repository.existsByNoAndMemberNo(qnaNo, member.getNo())){
            throw new AccessDeniedException("해당 QnA 글 접근 권한이 없습니다.");
        }
        repository.deleteById(qnaNo);
    }



    public Qna dtoToEntity(QnaDTO dto, Member member) {
        Qna entity = Qna.builder()
                .no(dto.getQnaNo())
                .title(dto.getTitle())
                .content(dto.getContent())
                .images(s3Service.convertListToString2(dto.getImages()))
                .qnaDate(dto.getQnaDate())
                .reply(dto.getReply())
                .replyDate(dto.getReplyDate())
                .status(dto.getStatus())
                .member(member)
                .build();
        return entity;
    };

    public QnaDTO entityToDto(Qna entity) {
        QnaDTO dto = QnaDTO.builder()
                .qnaNo(entity.getNo())
                .title(entity.getTitle())
                .content(entity.getContent())
                .images(s3Service.convertStringToList(entity.getImages()))
                .qnaDate(entity.getQnaDate())
                .reply(entity.getReply())
                .replyDate(entity.getReplyDate())
                .memberNo(entity.getMember().getNo())
                .code(entity.getMember().getCountry().getCode())
                .status(entity.isStatus())
                .build();
        return dto;
    };
}
