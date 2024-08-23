package com.v6.yeogaekgi.payTrack.repository;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class PayTrackQueryRepositoryImpl implements PayTrackQueryRepository {

    private final EntityManager em;

    @Override
    public List<Object[]> findPayTrackByUserCardNo(Long userCardNo, int year, int month) {

        String query = "SELECT COALESCE(p.user_card_no, t.user_card_no) AS user_card_no, " +
                "COALESCE(p.pay_date, t.tran_date) AS date_time, " +
                "p.pay_no, p.pay_type, p.pay_price, p.status, p.service_name, " +
                "COALESCE(p.transit_balance_snap, t.transit_balance_snap) AS transit_balance_snap, " +
                "COALESCE(p.pay_balance_snap, t.pay_balance_snap) AS pay_balance_snap, " +
                "t.tran_no, t.tran_type, t.transfer_type, t.krw_amount, t.foreign_amount, " +
                "t.currency_type " +
                "FROM (SELECT * FROM payment WHERE user_card_no = :userCardNo AND " +
                "YEAR(pay_date) =:year AND MONTH(pay_date) = :month) p " +
                "LEFT JOIN (SELECT * FROM transaction WHERE user_card_no = :userCardNo AND " +
                "YEAR(tran_date) =:year AND MONTH(tran_date) = :month) t " +
                "ON p.user_card_no = t.user_card_no AND p.pay_date = t.tran_date " +
                "UNION " +
                "SELECT COALESCE(p.user_card_no, t.user_card_no) AS user_card_no, " +
                "COALESCE(p.pay_date, t.tran_date) AS date_time, " +
                "p.pay_no, p.pay_type, p.pay_price, p.status, p.service_name, " +
                "COALESCE(p.transit_balance_snap, t.transit_balance_snap) AS transit_balance_snap, " +
                "COALESCE(p.pay_balance_snap, t.pay_balance_snap) AS pay_balance_snap, " +
                "t.tran_no, t.tran_type, t.transfer_type, t.krw_amount, t.foreign_amount, " +
                "t.currency_type " +
                "FROM (SELECT * FROM payment WHERE user_card_no = :userCardNo AND " +
                "YEAR(pay_date) =:year AND MONTH(pay_date) = :month) p " +
                "RIGHT JOIN (SELECT * FROM transaction WHERE user_card_no = :userCardNo AND " +
                "YEAR(tran_date) =:year AND MONTH(tran_date) = :month) t " +
                "ON p.user_card_no = t.user_card_no AND p.pay_date = t.tran_date " +
                "ORDER BY date_time DESC";

        return em.createNativeQuery(query)
                .setParameter("userCardNo", userCardNo)
                .setParameter("year", year)
                .setParameter("month", month)
                .getResultList();
    }
}
