package com.v6.yeogaekgi.review.repository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.JPQLQuery;
import com.v6.yeogaekgi.review.entity.QReview;
import com.v6.yeogaekgi.review.entity.Review;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public class ReviewListRepository extends QuerydslRepositorySupport {

    public ReviewListRepository() {
        super(Review.class);
    }

    public Slice<Review> listPage(Long servicesId, Pageable pageable) {
        QReview review = QReview.review;

        JPQLQuery<Review> jpqlQuery = this.from(review);

        BooleanBuilder booleanBuilder = new BooleanBuilder();
        BooleanExpression expression = review.status.eq(0)
                .and(review.services.id.eq(servicesId));

        booleanBuilder.and(expression);

        pageable.getSort().forEach(sortOrder -> {
            Order order = sortOrder.isAscending() ? Order.ASC : Order.DESC;
            PathBuilder<?> entityPath = new PathBuilder<>(Review.class, "review");
            jpqlQuery.orderBy(new OrderSpecifier(order, entityPath.get(sortOrder.getProperty())));
        });

        jpqlQuery.where(booleanBuilder);

        // Offset 설정
        jpqlQuery.offset(pageable.getOffset());

        // Limit 설정
        jpqlQuery.limit(pageable.getPageSize() + 1);


        List<Review> reviewList = jpqlQuery.fetch();
        boolean hasNext = reviewList.size() > pageable.getPageSize();

        if (hasNext) {
            reviewList.remove(pageable.getPageSize());
        }

        return new SliceImpl<>(reviewList, pageable, hasNext);
    }
}
