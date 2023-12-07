package com.dogfight.dogfight.api.service.comment;

import com.dogfight.dogfight.api.service.comment.request.CommentServiceRequest;
import com.dogfight.dogfight.api.service.comment.response.CommentResponse;

import java.util.List;

public interface CommentService {
    public String create(CommentServiceRequest request);
    public String update(CommentServiceRequest request, Long id);

    public String delete(Long commentId, String writer);

    public List<CommentResponse> findAllCommentByBoardId(Long boardId);

}
