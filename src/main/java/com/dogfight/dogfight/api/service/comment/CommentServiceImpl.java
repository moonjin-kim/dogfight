package com.dogfight.dogfight.api.service.comment;

import com.dogfight.dogfight.api.service.comment.request.CommentServiceRequest;
import com.dogfight.dogfight.api.service.comment.response.CommentResponse;
import com.dogfight.dogfight.config.error.CustomException;
import com.dogfight.dogfight.config.error.ErrorCode;
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
        log.info(request.getBoardId().toString());

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
                new CustomException("존재하지 않는 댓글입니다.", ErrorCode.COMMENT_NOT_FOUND)
        );

        if(!comment.getPassword().equals(request.getPassword())) {
            throw new CustomException("아이디 혹은 패스워드가 잘못되었습니다", ErrorCode.BAD_CREDENTIALS_EXCEPTION);
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
            throw new CustomException("아이디 혹은 패스워드가 잘못되었습니다", ErrorCode.BAD_CREDENTIALS_EXCEPTION);
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
