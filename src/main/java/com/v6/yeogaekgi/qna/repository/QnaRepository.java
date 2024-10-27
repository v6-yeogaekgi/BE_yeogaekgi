package com.v6.yeogaekgi.qna.repository;

import com.v6.yeogaekgi.qna.entity.Qna;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QnaRepository extends JpaRepository<Qna, Long> {
    Slice<Qna> findByMember_Id(Long memberNo, Pageable pageable);
}
