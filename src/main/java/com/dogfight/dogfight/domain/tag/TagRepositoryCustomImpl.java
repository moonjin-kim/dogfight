package com.dogfight.dogfight.domain.tag;

import com.dogfight.dogfight.domain.board.QBoard;
import com.dogfight.dogfight.domain.tag.response.TagListResponse;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Slf4j
public class TagRepositoryCustomImpl implements TagRepositoryCustom{
    private final JPAQueryFactory queryFactory;
    QBoard qBoard = QBoard.board;
    QTag qTag = QTag.tag;

    @Override
    public List<TagListResponse> getTagListAndCount() {
        List<Tuple> tags = queryFactory.select(
                qTag.id,
                qTag.name,
                qBoard.tag.name.count()
            )
                .from(qBoard)
                .join(qBoard.tag, qTag)
                .groupBy(qBoard.tag.name)
                .fetch();
        log.info("tagList");

        for(Tuple tag : tags) {
            log.info("tagName = {}", tag.get(qTag.name));
            log.info("tagName = {}", tag.get(qBoard.tag.name.count()));
        }
        return tags.stream()
                .map(tuple -> TagListResponse.builder()
                        .id(tuple.get(qTag.id))
                        .name(tuple.get(qTag.name))
                        .count(tuple.get(qBoard.tag.name.count()))
                        .build())
                .collect(Collectors.toList());
    }
}
