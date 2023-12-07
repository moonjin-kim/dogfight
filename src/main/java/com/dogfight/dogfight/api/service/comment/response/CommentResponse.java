package com.dogfight.dogfight.api.service.comment.response;

import com.dogfight.dogfight.api.service.user.response.UserServiceResponse;
import com.dogfight.dogfight.domain.comment.Comment;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Getter
@Slf4j
public class CommentResponse {
    Long id;
    String content;
    String nickname;
    List<CommentResponse> children = new ArrayList<>();



    @Builder
    public CommentResponse(Long id, String content, String nickname) {
        this.id = id;
        this.content = content;
        this.nickname = nickname;
    }

    @Override
    public String toString() {
        return "CommentResponse{" +
                "id=" + id +
                ", content='" + content + '\'' +
                ", nickname='" + nickname + '\'' +
                ", children=" + children +
                '}';
    }

    public static CommentResponse convertCommentToDto(Comment comment) {
        return comment.getIsDeleted() == Boolean.FALSE ?
                new CommentResponse(comment.getId(), "삭제된 댓글입니다.", null) :
                new CommentResponse(comment.getId(), comment.getContent(), comment.getNickname());
    }
}
