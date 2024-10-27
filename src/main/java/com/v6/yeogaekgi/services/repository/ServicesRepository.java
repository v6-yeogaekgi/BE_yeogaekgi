package com.v6.yeogaekgi.services.repository;

import com.amazonaws.services.ec2.model.ServiceType;
import com.v6.yeogaekgi.services.entity.Services;
import com.v6.yeogaekgi.services.entity.ServicesType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface ServicesRepository extends JpaRepository<Services,Long> {

    // 필터가 아에 없을 경우
    @Query("""
    SELECT s FROM Services s
    WHERE s.address LIKE %:area%
    """)
    List<Services> findAllServices(@Param("area") String area);


    // 필터가 있는데 type만 걸려 있는 경우
    @Query("""
    SELECT s FROM Services s 
    WHERE s.type IN :types
    AND s.address LIKE %:area%
    """)
    List<Services> findServicesByTypesAndArea(
            @Param("types") List<ServicesType> types,
            @Param("area") String area
    );


    //필터 type 걸려 있고 내가 좋아요한 부분만 보고 싶은 경우
    @Query("""
    SELECT s FROM Services s 
    WHERE s.type IN :types
    AND s.address LIKE %:area%
    AND s.no in (Select lk.services.no from ServicesLike lk where
    lk.member.no=:memberNo
    )
    """)
    List<Services> findServicesByTypesAndAreaAndMyLike(
            @Param("types") List<ServicesType> types,
            @Param("area") String area,
            @Param("memberNo") Long memberNo
    );

    // 필터 타입 안걸려 있고 내가 좋아요한 부분만 보고 싶은 경우
    @Query("""
    SELECT s FROM Services s 
    WHERE s.address LIKE %:area%
    AND s.no in (Select lk.services.no from ServicesLike lk where
    lk.member.no=:memberNo
    )
    """)
    List<Services> findServicesByAreaAndMyLike(
            @Param("area") String area,
            @Param("memberNo") Long memberNo
    );

    // 필터 타입 걸려 있고 내가 쓴 리뷰만 보고 싶은 경우
    @Query("""
    SELECT s FROM Services s
    WHERE s.type IN :types
    AND s.address LIKE %:area%
    AND s.no in (select r.services.no from Review r where r.member.no =:memberNo)
    """)
    List<Services> findServicesByTypesAndAreaAndMyReview(
            @Param("types") List<ServicesType> types,
            @Param("area") String area,
            @Param("memberNo") Long memberNo
    );

    // 필터 타입 안 걸려 있고 내가 쓴 리뷰만 보고 싶은 경우
    @Query("""
    SELECT s FROM Services s
    WHERE s.address LIKE %:area%
    AND s.no in (select r.services.no from Review r where r.member.no =:memberNo)
    """)
    List<Services> findServicesByAreaAndMyReview(
            @Param("area") String area,
            @Param("memberNo") Long memberNo
    );

    //필터 타입 걸려 있고 내가 쓴 리뷰 및 좋아요 보고 싶은 경우
    @Query("""
    SELECT s FROM Services s
    WHERE s.type IN :types 
    AND s.address LIKE %:area%
    AND (s.no in (select r.services.no from Review r where r.member.no =:memberNo)
    OR s.no in (Select lk.services.no from ServicesLike lk where
    lk.member.no=:memberNo)
    )""")
    List<Services> findServicesByTypesAndAreaAndMyReviewANDmyLike(
            @Param("types") List<ServicesType> types,
            @Param("area") String area,
            @Param("memberNo") Long memberNo
    );

    @Query("""
    SELECT s FROM Services s
    WHERE s.address LIKE %:area%
    AND (s.no in (select r.services.no from Review r where r.member.no =:memberNo)
    OR s.no in (Select lk.services.no from ServicesLike lk where
    lk.member.no=:memberNo)
    )""")
    List<Services> findServicesByAreaAndMyReviewANDmyLike(
            @Param("area") String area,
            @Param("memberNo") Long memberNo
    );


    @Query("""
        SELECT s.name FROM Services s
        WHERE s.no =:servicesNo
    """)
    String findServiceNameByNo(@Param("servicesNo") Long servicesNo);
}
