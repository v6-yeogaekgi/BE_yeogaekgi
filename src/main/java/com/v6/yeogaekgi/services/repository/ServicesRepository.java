package com.v6.yeogaekgi.services.repository;

import com.v6.yeogaekgi.services.entity.Services;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ServicesRepository extends JpaRepository<Services,Long> {

}
