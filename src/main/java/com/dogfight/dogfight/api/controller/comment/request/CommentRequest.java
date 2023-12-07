package com.dogfight.dogfight.api.controller.comment.request;

import com.dogfight.dogfight.api.service.comment.request.CommentServiceRequest;
import lombok.Builder;

public class CommentRequest {
    String content;
    String nickname;
    String password;
    Long boardId;
    Long parentId;

    @Builder
    public CommentRequest(String content, String nickname, String password, Long boardId, Long parentId) {
        this.content = content;
        this.nickname = nickname;
        this.password = password;
        this.boardId = boardId;
        this.parentId = parentId;
    }

    public CommentServiceRequest toCommentServiceRequest() {
        return CommentServiceRequest.builder()
                .content(content)
                .nickname(nickname)
                .password(password)
                .boardId(boardId)
                .parentId(parentId)
                .build();
    }
}
