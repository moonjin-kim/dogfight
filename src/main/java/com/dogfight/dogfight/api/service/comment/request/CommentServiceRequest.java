package com.dogfight.dogfight.api.service.comment.request;

import com.dogfight.dogfight.domain.board.Board;
import com.dogfight.dogfight.domain.comment.Comment;
import com.dogfight.dogfight.domain.user.User;
import lombok.Builder;

public class CommentServiceRequest {
    String content;
    String account;
    Long boardId;
    Long parentId;

    @Builder
    public CommentServiceRequest(String content, String account, Long boardId, Long parentId) {
        this.content = content;
        this.account = account;
        this.boardId = boardId;
        this.parentId = parentId;
    }

    public Comment toEntity(User user, Board board, Comment parent){
        return Comment.builder()
                .content(content)
                .board(board)
                .parent(parent)
                .user(user)
                .build();
    }
}
