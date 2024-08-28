package com.v6.yeogaekgi.services.repository;

import com.v6.yeogaekgi.services.entity.ServiceLike;
import com.v6.yeogaekgi.services.entity.Services;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;


public interface ServicesRepository extends JpaRepository<Services,Long> {

    @Query("""
    select s.likeCnt
    from Services s
    where s.id =:servicesId
""")
    int like (Long servicesId);

}
