package com.v6.yeogaekgi.payTrack.repository;

import com.v6.yeogaekgi.payTrack.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Optional<Payment> findById(Long id);

    @Query("""
    select p from Payment p
    where p.member.id = :memberId
    and p.services is not null
""")
    List<Payment> findAllPaymentsByMemberIdWithNonNullService(@Param("memberId") Long memberId);


    @Query("""
    select p from Payment p
    where p.member.no = :memberNo
    and p.serviceName LIKE %:servicesName%
    and p.no = :payNo
""")
    Optional<Payment> findByMemberNoAndServiceName(@Param("memberNo") Long memberNo,
                                                   @Param("servicesName") String servicesName, Long payNo);
}
