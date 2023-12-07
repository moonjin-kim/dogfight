package com.dogfight.dogfight.api.service.comment;

import com.dogfight.dogfight.api.service.comment.request.CommentServiceRequest;
import com.dogfight.dogfight.api.service.comment.response.CommentResponse;
import com.dogfight.dogfight.domain.board.Board;
import com.dogfight.dogfight.domain.board.BoardRepository;
import com.dogfight.dogfight.domain.comment.Comment;
import com.dogfight.dogfight.domain.comment.CommentRepository;
import com.dogfight.dogfight.domain.user.User;
import com.dogfight.dogfight.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommentServiceImpl implements CommentService{
    private final UserRepository userRepository;
    private final BoardRepository boardRepository;
    private final CommentRepository commentRepository;
    @Override
    public String create(CommentServiceRequest request) {
        User user = userRepository.findByAccount(request.getAccount());
        if(user == null) {
            throw new NullPointerException("존재하지 않는 유저입니다.");
        }

        Board board = boardRepository.findById(request.getBoardId()).orElseThrow(() ->
                new NullPointerException("존재하지 않는 게시글입니다.")
        );

        Comment parent = null;
        if(request.getParentId() != null) {
            parent = commentRepository.findById(request.getParentId()).orElseThrow(() ->
                    new NullPointerException("존재하지 않는 댓글입니다.") );
        }
        Comment comment = request.toEntity(user,board,parent);
        commentRepository.save(comment);

        return "ok";
    }

    @Override
    public String update(CommentServiceRequest request,Long id) {
        User user = userRepository.findByAccount(request.getAccount());
        if(user == null) {
            throw new NullPointerException("존재하지 않는 유저입니다.");
        }

        Comment comment = commentRepository.findById(id).orElseThrow(() ->
            new NullPointerException("존재하지 않는 댓글입니다.")
        );

        //댓글을 수정 가능한 유저인지 확인
        if(!Objects.equals(comment.getUser().getId(), user.getId())) {
            throw new NullPointerException("댓글을 수정할 수 없습니다");
        }

        comment.updateContent(request.getContent());
        commentRepository.save(comment);

        return "ok";
    }

    @Override
    public String delete(Long id, String account) {
        User user = userRepository.findByAccount(account);
        if(user == null) {
            throw new NullPointerException("존재하지 않는 유저입니다.");
        }

        Comment comment = commentRepository.findById(id).orElseThrow(() ->
                new NullPointerException("존재하지 않는 댓글입니다.")
        );

        //댓글을 수정 가능한 유저인지 확인
        if(!Objects.equals(comment.getUser().getId(), user.getId())) {
            throw new NullPointerException("댓글을 수정할 수 없습니다");
        }

        comment.changeIsDeleted(true);
        commentRepository.save(comment);

        return "ok";
    }

    @Override
    public List<CommentResponse> findAllCommentByBoardId(Long boardId) {
        return commentRepository.findAllByBoard(boardId);
    }
}
