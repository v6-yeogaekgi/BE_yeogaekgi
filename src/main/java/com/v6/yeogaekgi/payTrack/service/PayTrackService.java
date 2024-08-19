package com.v6.yeogaekgi.payTrack.service;

import com.v6.yeogaekgi.payTrack.repository.PaymentRepository;
import com.v6.yeogaekgi.payTrack.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PayTrackService {

    private final PaymentRepository paymentRepository;

    private final TransactionRepository transactionRepository;

 /*
 *  SELECT
    COALESCE(p.user_card_no, t.user_card_no) AS user_card_no,
    COALESCE(p.pay_date, t.tran_date) AS date_time,
    p.pay_no, p.pay_type, p.pay_price, p.status, p.service_name,
    p.transit_balance_snap AS p_transit_balance_snap,
    p.pay_balance_snap AS p_pay_balance_snap,
    t.tran_no, t.tran_type, t.transfer_type, t.krw_amount, t.foreign_amount,
    t.transit_balance_snap AS t_transit_balance_snap,
    t.pay_balance_snap AS t_pay_balance_snap
FROM
    (SELECT * FROM payment WHERE user_card_no = ?) p
LEFT JOIN
    (SELECT * FROM transaction WHERE user_card_no = ?) t
ON
    p.user_card_no = t.user_card_no AND p.pay_date = t.tran_date

UNION

SELECT
    COALESCE(p.user_card_no, t.user_card_no) AS user_card_no,
    COALESCE(p.pay_date, t.tran_date) AS date_time,
    p.pay_no, p.pay_type, p.pay_price, p.status, p.service_name,
    p.transit_balance_snap AS p_transit_balance_snap,
    p.pay_balance_snap AS p_pay_balance_snap,
    t.tran_no, t.tran_type, t.transfer_type, t.krw_amount, t.foreign_amount,
    t.transit_balance_snap AS t_transit_balance_snap,
    t.pay_balance_snap AS t_pay_balance_snap
FROM
    (SELECT * FROM payment WHERE user_card_no = ?) p
RIGHT JOIN
    (SELECT * FROM transaction WHERE user_card_no = ?) t
ON
    p.user_card_no = t.user_card_no AND p.pay_date = t.tran_date

ORDER BY
    date_time DESC;
 *
 *
*/
//    public List<>
}
