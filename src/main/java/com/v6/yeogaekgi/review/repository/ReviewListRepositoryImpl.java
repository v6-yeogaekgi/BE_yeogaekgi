package com.v6.yeogaekgi.review.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.JPQLQuery;
import com.v6.yeogaekgi.review.entity.QReview;
import com.v6.yeogaekgi.review.entity.Review;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;

public class ReviewListRepositoryImpl extends QuerydslRepositorySupport implements ReviewListRepository {
    public ReviewListRepositoryImpl() {
        super(Review.class);
    }

    public Slice<Review> listPage(Long serviceId, Pageable pageable, Integer payStatus) {
        QReview review = QReview.review;

        JPQLQuery<Review> jpqlQuery = this.from(review);

        BooleanBuilder booleanBuilder = new BooleanBuilder();
        BooleanExpression expression = review.status.eq(0)
                .and(review.services.id.eq(serviceId));

        booleanBuilder.and(expression);

        if (payStatus != null) {
            BooleanExpression condition = review.payment.isNotNull();
            booleanBuilder.and(condition);
        }

        pageable.getSort().forEach(sortOrder -> {
            Order order = sortOrder.isAscending() ? Order.ASC : Order.DESC;
            PathBuilder<?> entityPath = new PathBuilder<>(Review.class, "review");
            jpqlQuery.orderBy(new OrderSpecifier(order, entityPath.get(sortOrder.getProperty())));
        });

        jpqlQuery.where(booleanBuilder);

        jpqlQuery.limit(pageable.getPageSize() + 1);

        List<Review> reviewList = jpqlQuery.fetch();
        boolean hasNext = reviewList.size() > pageable.getPageSize();

        if (hasNext) {
            reviewList.remove(pageable.getPageSize());
        }

        return new SliceImpl<>(reviewList, pageable, hasNext);
    }
}
