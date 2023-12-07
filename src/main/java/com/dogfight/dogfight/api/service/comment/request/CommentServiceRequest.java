package com.dogfight.dogfight.api.service.comment.request;

import com.dogfight.dogfight.domain.board.Board;
import com.dogfight.dogfight.domain.comment.Comment;
import com.dogfight.dogfight.domain.user.User;
import lombok.Builder;
import lombok.Getter;

@Getter
public class CommentServiceRequest {
    String content;
    String nickname;
    String password;
    Long boardId;
    Long parentId;

    @Builder
    public CommentServiceRequest(String content, String nickname, String password, Long boardId, Long parentId) {
        this.content = content;
        this.nickname = nickname;
        this.password = password;
        this.boardId = boardId;
        this.parentId = parentId;
    }

    public Comment toEntity(Board board, Comment parent){
        return Comment.builder()
                .content(content)
                .board(board)
                .parent(parent)
                .nickname(nickname)
                .password(password)
                .build();
    }
}
