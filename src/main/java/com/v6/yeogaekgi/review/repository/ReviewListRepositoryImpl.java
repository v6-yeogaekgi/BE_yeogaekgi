package com.v6.yeogaekgi.review.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPQLQuery;
import com.v6.yeogaekgi.member.entity.QMember;
import com.v6.yeogaekgi.review.entity.QReview;
import com.v6.yeogaekgi.review.entity.Review;
import com.v6.yeogaekgi.services.entity.QServices;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;

public class ReviewListRepositoryImpl extends QuerydslRepositorySupport implements ReviewListRepository {
    public ReviewListRepositoryImpl() {
        super(Review.class);
    }

    public Slice<Review> listPage(Long serviceId,Pageable pageable,Integer payStatus) {
        QReview review = QReview.review;
//        QMember member = QMember.member;
//        QServices services = QServices.services;
//        QPayment payment = QPayment.payment;
        JPQLQuery<Review> jpqlQuery = this.from(review);
//        jpqlQuery.leftJoin(member).on(review.member.eq(member)); 내가 쓴 리뷰 보기 설정 할떄 사용할 수 있을뜻
//        jpqlQuery.leftJoin(services).on(review.services.eq(services));
//        jpqlQuery.leftJoin(payment).on(review.payment.eq(payment));

        jpqlQuery.orderBy(review.modDate.desc());


        JPQLQuery<Review> tuple = jpqlQuery.select(review);

        BooleanBuilder booleanBuilder = new BooleanBuilder();
        BooleanExpression expression = review.status.eq(1)
                .and(review.services.id.eq(serviceId));

        booleanBuilder.and(expression);

        if (payStatus != null) {
            BooleanExpression condition = review.payment.isNotNull();
            booleanBuilder.and(condition);
        }

        tuple.where(booleanBuilder);

        jpqlQuery.limit(pageable.getPageSize());
        List<Review> result = tuple.fetch();

        boolean hasNext = result.size() > pageable.getPageSize();

        if (hasNext) {
            result.remove(pageable.getPageSize());
        }

        return new SliceImpl<>(result, pageable, hasNext);
    }

}
