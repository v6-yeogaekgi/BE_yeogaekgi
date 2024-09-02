package com.v6.yeogaekgi.services.repository;

import com.amazonaws.services.ec2.model.ServiceType;
import com.v6.yeogaekgi.services.entity.Services;
import com.v6.yeogaekgi.services.entity.ServicesType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;


public interface ServicesRepository extends JpaRepository<Services,Long> {
    @Query("""
    SELECT s FROM Services s
    WHERE s.address LIKE %:area%
    """)
    List<Services> findAllServices(@Param("area") String area);

    @Query("""
    SELECT s FROM Services s 
    WHERE s.type IN :types
    AND s.address LIKE %:area%
    """)
        List<Services> findServicesByTypesAndArea(
                @Param("types") List<ServicesType> types,
                @Param("area") String area
        );

    @Query("""
        SELECT s.name FROM Services s
        WHERE s.id =:servicesId
    """)
    String findServiceNameById(@Param("servicesId") Long servicesId);
}
