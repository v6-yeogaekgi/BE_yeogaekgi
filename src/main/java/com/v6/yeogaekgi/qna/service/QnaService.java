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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Log4j2
@RequiredArgsConstructor

public class QnaService {
    private final QnaRepository repository;
    private final S3Service s3Service;

    // Qna List
    public PageResultDTO<QnaDTO> getQnaList(PageRequestDTO pageRequestDTO, Member member) {
        Pageable pageable = PageRequest.of(pageRequestDTO.getPage(), 10, Sort.by(Sort.Direction.DESC, "no"));
        Slice<Qna> result = repository.findByMember_No(member.getNo(), pageable);
        if(result != null){
            List<QnaDTO> content = result.getContent().stream()
                    .map(Qna -> entityToDto(Qna))
                    .collect(Collectors.toList());
            PageResultDTO<QnaDTO> pageResultDTO = new PageResultDTO<>(content, pageable, result.hasNext());

            return pageResultDTO;
        }
        return null;
    }

    // Qna Detial
    public QnaDTO getQna(Long qnaNo) {
        Optional<Qna> qna = repository.findById(qnaNo);
        QnaDTO dto = null;
        if(qna.isPresent()){
            dto = entityToDto(qna.get());
        }
        return dto;
    }

    // Qna Register
    public Long register(Qna qna) {
        repository.save(qna);
        return qna.getNo();
    }

    // Qna modify
    public void modify(Qna qna) {
        Optional<Qna> result = repository.findById(qna.getNo());
        if(result.isPresent()){
            repository.save(qna);
        }
    }

    // Qna remove
    public void remove(Long postNo) {
        Optional<Qna> result = repository.findById(postNo);
        if(result.isPresent()){
            repository.deleteById(postNo);
        }
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
