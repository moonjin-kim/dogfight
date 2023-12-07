package com.dogfight.dogfight.api.service.comment;

import com.dogfight.dogfight.api.service.comment.request.CommentServiceRequest;
import com.dogfight.dogfight.api.service.comment.response.CommentResponse;
import com.dogfight.dogfight.domain.board.Board;
import com.dogfight.dogfight.domain.board.BoardRepository;
import com.dogfight.dogfight.domain.comment.Comment;
import com.dogfight.dogfight.domain.comment.CommentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommentServiceImpl implements CommentService{
    private final BoardRepository boardRepository;
    private final CommentRepository commentRepository;
    @Override
    public Long create(CommentServiceRequest request) {

        Board board = boardRepository.findById(request.getBoardId()).orElseThrow(() ->
                new NullPointerException("존재하지 않는 게시글입니다.")
        );

        Comment parent = null;
        if(request.getParentId() != null) {
            parent = commentRepository.findById(request.getParentId()).orElseThrow(() ->
                    new NullPointerException("존재하지 않는 댓글입니다.") );
        }
        Comment comment = request.toEntity(board,parent);
        Comment result = commentRepository.save(comment);

        return result.getId();
    }

    @Override
    public Long update(CommentServiceRequest request,Long id) {


        Comment comment = commentRepository.findById(id).orElseThrow(() ->
            new NullPointerException("존재하지 않는 댓글입니다.")
        );

        if(!comment.getPassword().equals(request.getPassword())) {
            throw new NullPointerException("잘못된 비밀번호 입니다");
        }

        comment.updateContent(request.getContent());
        Comment result = commentRepository.save(comment);

        return result.getId();
    }

    @Override
    public Long delete(Long id, String password) {

        Comment comment = commentRepository.findById(id).orElseThrow(() ->
                new NullPointerException("존재하지 않는 댓글입니다.")
        );

        if(!comment.getPassword().equals(password)) {
            throw new NullPointerException("잘못된 비밀번호 입니다");
        }

        comment.changeIsDeleted(true);
        Comment result = commentRepository.save(comment);

        return result.getId();
    }

    @Override
    public List<CommentResponse> findAllCommentByBoardId(Long boardId) {
        Board board = boardRepository.findById(boardId).orElseThrow(() ->
                new NullPointerException("존재하지 않는 게시글입니다.")
        );

        return commentRepository.findAllByBoard(boardId);
    }
}
