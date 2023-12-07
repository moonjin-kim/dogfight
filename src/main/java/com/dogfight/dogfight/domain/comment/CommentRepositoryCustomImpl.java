package com.dogfight.dogfight.domain.comment;

import com.dogfight.dogfight.api.service.comment.response.CommentResponse;
import com.dogfight.dogfight.domain.board.QBoard;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.dogfight.dogfight.api.service.comment.response.CommentResponse.convertCommentToDto;

@RequiredArgsConstructor
public class CommentRepositoryCustomImpl implements CommentRepositoryCustom{
    private final JPAQueryFactory queryFactory;
    QComment qComment = QComment.comment;

    @Override
    public List<CommentResponse> findAllByBoard(Long id) {
        List<Comment> comments = queryFactory.selectFrom(qComment)
                .leftJoin(qComment.parent).fetchJoin()
                .where(qComment.board.id.eq(id))
                .orderBy(qComment.parent.id.asc().nullsFirst(),
                        qComment.createdDateTime.asc())
                .fetch();

        List<CommentResponse> commentResponseDTOList = new ArrayList<>();
        Map<Long, CommentResponse> commentDTOHashMap = new HashMap<>();

        comments.forEach(c -> {
            CommentResponse commentResponseDTO = convertCommentToDto(c);
            commentDTOHashMap.put(commentResponseDTO.getId(), commentResponseDTO);
            if (c.getParent() != null) commentDTOHashMap.get(c.getParent().getId()).getChildren().add(commentResponseDTO);
            else commentResponseDTOList.add(commentResponseDTO);
        });
        return commentResponseDTOList;
    }
}
