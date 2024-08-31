package com.v6.yeogaekgi.services.repository;

import com.amazonaws.services.ec2.model.ServiceType;
import com.v6.yeogaekgi.services.entity.Services;
import com.v6.yeogaekgi.services.entity.ServicesType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface ServicesRepository extends JpaRepository<Services,Long> {

    @Query("SELECT s FROM Services s WHERE s.type IN :types")
    List<Services> findByTypes(@Param("types") List<ServicesType> type);

}
