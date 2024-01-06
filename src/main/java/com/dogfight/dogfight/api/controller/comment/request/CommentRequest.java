package com.dogfight.dogfight.api.controller.comment.request;

import com.dogfight.dogfight.api.service.comment.request.CommentServiceRequest;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@NoArgsConstructor
public class CommentRequest {
    String content;
    String nickname;
    String password;
    Long select;
    Long boardId;
    Long parentId;

    @Builder
    public CommentRequest(String content, String nickname, Long select,String password, Long boardId, Long parentId) {
        this.content = content;
        this.nickname = nickname;
        this.password = password;
        this.select = select;
        this.boardId = boardId;
        this.parentId = parentId;
    }

    public CommentServiceRequest toCommentServiceRequest() {
        return CommentServiceRequest.builder()
                .content(content)
                .nickname(nickname)
                .select(select)
                .password(password)
                .boardId(boardId)
                .parentId(parentId)
                .build();
    }
}
