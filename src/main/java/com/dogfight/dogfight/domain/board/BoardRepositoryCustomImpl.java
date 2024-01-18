package com.dogfight.dogfight.domain.board;

import com.dogfight.dogfight.api.service.board.response.BoardResponse;
import com.dogfight.dogfight.domain.tag.QTag;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class BoardRepositoryCustomImpl implements BoardRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    QBoard qBoard = QBoard.board;
    QTag qTag = QTag.tag;
    @Override
    public Page<Board> search(Pageable pageable,String title, String tag) {
        List<Board> result = queryFactory.selectFrom(qBoard)
                .leftJoin(qBoard.tag, qTag)
                .where(eqTag(tag), containTag(title))
                .orderBy(getSortCondition(pageable.getSort()))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return new PageImpl<>(result);
    }


    private BooleanExpression eqTag(String tag) {
        if (tag == null || tag.isEmpty()) {
            return null;
        }
        return qBoard.tag.name.eq(tag);
    }

    private BooleanExpression containTag(String title) {
        if (title == null || title.isEmpty()) {
            return null;
        }
        return qBoard.title.contains(title);
    }

    @Override
    public Board increaseViewsAndReturnBoard(long id) {
        queryFactory.update(qBoard)
                .set(qBoard.views, qBoard.views.add(1))
                .where(qBoard.id.eq(id))
                .execute();

        return queryFactory
                .selectFrom(qBoard)
                .where(qBoard.id.eq(id))
                .fetchOne();
    }

    @Override
    public BoardResponse selectRandBoard() {
        Board board = queryFactory.selectFrom(qBoard)
                .orderBy(Expressions.numberTemplate(Double.class, "function('rand')").asc())
                .fetchFirst();
        return BoardResponse.of(board);
    }

    private OrderSpecifier[] getSortCondition(final Sort sort) {
        final List<OrderSpecifier> orders = new ArrayList<>();

        if(sort.isEmpty()) {
            return new OrderSpecifier[]{new OrderSpecifier(Order.DESC, qBoard.id)};
        }

        for(final Sort.Order sortOrder : sort) {
            addOrderSpecifierByCurrentSortCondition(sortOrder, orders);
        }
        return orders.toArray(new OrderSpecifier[0]);
    }

    private void addOrderSpecifierByCurrentSortCondition(final Sort.Order sortOrder,
                                                         final List<OrderSpecifier> specifiers) {
        Order direction = Order.DESC;
        if (sortOrder.isAscending()){
            direction = Order.ASC;
        }

        final String orderTarget = sortOrder.getProperty();
        if(orderTarget.equals("popular")) {
            specifiers.add(new OrderSpecifier<>(direction, qBoard.views));
            return;
        }
        if(orderTarget.equals("id")){
            specifiers.add(new OrderSpecifier(direction, qBoard.id));
        }

    }


}
