package com.dogfight.dogfight.api.service.comment;

import com.dogfight.dogfight.api.service.comment.request.CommentServiceRequest;
import com.dogfight.dogfight.api.service.comment.response.CommentResponse;

import java.util.List;

public interface CommentService {
    public Long create(CommentServiceRequest request);
    public Long update(CommentServiceRequest request, Long id);

    public Long delete(Long commentId, String password);

    public List<CommentResponse> findAllCommentByBoardId(Long boardId);

}
