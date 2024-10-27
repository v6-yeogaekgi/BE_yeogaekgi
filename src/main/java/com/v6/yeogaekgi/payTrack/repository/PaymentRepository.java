package com.v6.yeogaekgi.payTrack.repository;

import com.v6.yeogaekgi.payTrack.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    @Query("""
    select p from Payment p
    where p.member.no = :memberNo
    and p.services is not null
""")
    List<Payment> findAllPaymentsByMemberNoWithNonNullService(@Param("memberNo") Long memberNo);


    @Query("""
    select p from Payment p
    where p.member.no = :memberNo
    and p.serviceName LIKE %:serviceName%
    and p.no = :payNo
""")
    Optional<Payment> findByMemberNoAndServiceName(@Param("memberNo") Long memberNo,
                                                   @Param("serviceName") String serviceName, Long payNo);
}
