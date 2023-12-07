package com.dogfight.dogfight.api.controller.comment.request;

import com.dogfight.dogfight.api.service.comment.request.CommentServiceRequest;
import lombok.Builder;

public class CommentRequest {
    String content;
    String account;
    Long boardId;
    Long parentId;

    @Builder
    public CommentRequest(String content, String account, Long boardId, Long parentId) {
        this.content = content;
        this.account = account;
        this.boardId = boardId;
        this.parentId = parentId;
    }

    public CommentServiceRequest toCommentServiceRequest(String account) {
        return CommentServiceRequest.builder()
                .content(content)
                .account(account)
                .boardId(boardId)
                .parentId(parentId)
                .build();
    }
}
