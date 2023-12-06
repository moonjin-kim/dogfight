package com.dogfight.dogfight.domain.comment;

import com.dogfight.dogfight.api.service.comment.response.CommentResponse;

import java.util.List;

public interface CommentRepositoryCustom {
    public List<CommentResponse> findAllByBoard(Long id);
}
