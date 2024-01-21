package com.dogfight.dogfight.api.service.comment;

import com.dogfight.dogfight.IntegrationTestSupport;
import com.dogfight.dogfight.api.service.board.BoardServiceImpl;
import com.dogfight.dogfight.api.service.board.request.BoardCreateServiceRequest;
import com.dogfight.dogfight.api.service.board.response.BoardResponse;
import com.dogfight.dogfight.api.service.comment.request.CommentServiceRequest;
import com.dogfight.dogfight.api.service.comment.response.CommentResponse;
import com.dogfight.dogfight.common.filehandler.FileHandler;
import com.dogfight.dogfight.domain.board.BoardRepository;
import com.dogfight.dogfight.domain.comment.Comment;
import com.dogfight.dogfight.domain.comment.CommentRepository;
import com.dogfight.dogfight.domain.tag.TagRepository;
import com.dogfight.dogfight.domain.user.Role;
import com.dogfight.dogfight.domain.user.User;
import com.dogfight.dogfight.domain.user.UserRepository;
import com.dogfight.dogfight.domain.vote.VoteRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;

import java.io.FileInputStream;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


@Slf4j
class CommentServiceImplTest extends IntegrationTestSupport {
    @Autowired
    BoardServiceImpl boardService;
    @Autowired
    BoardRepository boardRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    TagRepository tagRepository;
    @Autowired
    VoteRepository voteRepository;
    @Autowired
    CommentService commentService;
    @Autowired
    CommentRepository commentRepository;
    @Autowired
    FileHandler fileHandler;

    final String fileName1 = "messi";
    final String contentType1 = "jpeg";
    final String fileName2 = "ronaldo";
    final String contentType2 = "jpeg";
    final String filePath1 = "src/test/resources/testImage/"+fileName1+"."+contentType1;
    final String filePath2 = "src/test/resources/testImage/"+fileName2+"."+contentType2;

    @AfterEach
    void tearDown() {
        commentRepository.deleteAll();
        boardRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
        tagRepository.deleteAllInBatch();
        voteRepository.deleteAllInBatch();
        fileHandler.deleteFolder();
    }

    @DisplayName("존재하는 게시글에 댓글을 작성할 수 있다.")
    @Test
    void createComment() throws Exception{
        //given
        LocalDateTime registeredDateTime = LocalDateTime.now();
        FileInputStream fileInputStream1 = new FileInputStream(filePath1);
        FileInputStream fileInputStream2 = new FileInputStream(filePath2);
        MockMultipartFile image1 = new MockMultipartFile(
                "images",
                fileName1 + "." + contentType1,
                "/testImage/" + contentType1,
                fileInputStream1);

        MockMultipartFile image2 = new MockMultipartFile(
                "images",
                fileName2 + "." + contentType2,
                "/testImage/" + contentType2,
                fileInputStream2);

        String writer = "testUser";
        User user = User.builder()
                .account("testUser")
                .password("!test12345")
                .nickname(writer)
                .email("test123@gmail.com")
                .role(Role.USER)
                .build();

        userRepository.save(user);

        BoardCreateServiceRequest boardRequest = BoardCreateServiceRequest.builder()
                .title("축구선수 Goat는?")
                .writer(writer)
                .tag("스포츠")
                .content("메시 vs 호날두")
                .option1("메시")
                .option1Image(image1)
                .option2("호날두")
                .option2Image(image2)
                .build();

        BoardResponse boardResponse = boardService.create(boardRequest,registeredDateTime);

        CommentServiceRequest commentRequest = CommentServiceRequest.builder()
                .content("행복합니다")
                .boardId(boardResponse.getId())
                .nickname("testUser")
                .password("1234")
                .build();

        //when
        Long result = commentService.create(commentRequest);
        List<Comment> comments = commentRepository.findAll();

        //then
        assertThat(comments).hasSize(1);
    }

    @DisplayName("대댓글을 작성할 수 있다.")
    @Test
    void createChildrenComment() throws Exception{
        //given
        LocalDateTime registeredDateTime = LocalDateTime.now();
        FileInputStream fileInputStream1 = new FileInputStream(filePath1);
        FileInputStream fileInputStream2 = new FileInputStream(filePath2);
        MockMultipartFile image1 = new MockMultipartFile(
                "images",
                fileName1 + "." + contentType1,
                "/testImage/" + contentType1,
                fileInputStream1);

        MockMultipartFile image2 = new MockMultipartFile(
                "images",
                fileName2 + "." + contentType2,
                "/testImage/" + contentType2,
                fileInputStream2);

        String writer = "testUser";
        User user = User.builder()
                .account("testUser")
                .password("!test12345")
                .nickname(writer)
                .email("test123@gmail.com")
                .role(Role.USER)
                .build();

        userRepository.save(user);

        BoardCreateServiceRequest boardRequest = BoardCreateServiceRequest.builder()
                .title("축구선수 Goat는?")
                .writer(writer)
                .tag("스포츠")
                .content("메시 vs 호날두")
                .option1("메시")
                .option1Image(image1)
                .option2("호날두")
                .option2Image(image2)
                .build();

        BoardResponse boardResponse = boardService.create(boardRequest,registeredDateTime);

        CommentServiceRequest commentRequest = CommentServiceRequest.builder()
                .content("행복합니다")
                .boardId(boardResponse.getId())
                .nickname("testUser")
                .password("1234")
                .build();

        //when
        Long result = commentService.create(commentRequest);

        CommentServiceRequest commentChildrenRequest = CommentServiceRequest.builder()
                .content("행복합니다")
                .boardId(boardResponse.getId())
                .parentId(result)
                .nickname("testUser")
                .password("1234")
                .build();

        commentService.create(commentChildrenRequest);

        List<Comment> comments = commentRepository.findAll();
        //then
        assertThat(comments).hasSize(2);
    }

    @DisplayName("존재하지 않는 게시글에 댓글을 작성하면 에러가 발생한다")
    @Test
    void createCommentNoExistBoard() throws Exception{
        //given
        LocalDateTime registeredDateTime = LocalDateTime.now();

        CommentServiceRequest commentRequest = CommentServiceRequest.builder()
                .content("행복합니다")
                .boardId(1L)
                .nickname("testUser")
                .password("1234")
                .build();

        //when then
        assertThatThrownBy(() -> commentService.create(commentRequest))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("존재하지 않는 게시글입니다.");
    }

    @DisplayName("게시글의 댓글을 조회할 수 있다.")
    @Test
    void readComment() throws Exception{
        //given
        LocalDateTime registeredDateTime = LocalDateTime.now();
        FileInputStream fileInputStream1 = new FileInputStream(filePath1);
        FileInputStream fileInputStream2 = new FileInputStream(filePath2);
        MockMultipartFile image1 = new MockMultipartFile(
                "images",
                fileName1 + "." + contentType1,
                "/testImage/" + contentType1,
                fileInputStream1);

        MockMultipartFile image2 = new MockMultipartFile(
                "images",
                fileName2 + "." + contentType2,
                "/testImage/" + contentType2,
                fileInputStream2);

        String writer = "testUser";
        User user = User.builder()
                .account("testUser")
                .password("!test12345")
                .nickname(writer)
                .email("test123@gmail.com")
                .role(Role.USER)
                .build();

        userRepository.save(user);

        BoardCreateServiceRequest boardRequest = BoardCreateServiceRequest.builder()
                .title("축구선수 Goat는?")
                .writer(writer)
                .tag("스포츠")
                .content("메시 vs 호날두")
                .option1("메시")
                .option1Image(image1)
                .option2("호날두")
                .option2Image(image2)
                .build();

        BoardResponse boardResponse = boardService.create(boardRequest,registeredDateTime);

        CommentServiceRequest commentRequest1 = CommentServiceRequest.builder()
                .content("행복합니다")
                .boardId(boardResponse.getId())
                .nickname("testUser")
                .password("1234")
                .build();

        CommentServiceRequest commentRequest2 = CommentServiceRequest.builder()
                .content("행복합니다2")
                .boardId(boardResponse.getId())
                .nickname("testUser2")
                .password("1234")
                .build();

        commentService.create(commentRequest1);
        commentService.create(commentRequest2);

        //when
        List<CommentResponse> responseList = commentService.findAllCommentByBoardId(boardResponse.getId());

        //then
        assertThat(responseList).hasSize(2);
        log.info(responseList.get(0).toString());
    }

    @DisplayName("게시글의 댓글을 수정할 수 있다")
    @Test
    void updateComment() throws Exception{
        //given
        LocalDateTime registeredDateTime = LocalDateTime.now();
        FileInputStream fileInputStream1 = new FileInputStream(filePath1);
        FileInputStream fileInputStream2 = new FileInputStream(filePath2);
        MockMultipartFile image1 = new MockMultipartFile(
                "images",
                fileName1 + "." + contentType1,
                "/testImage/" + contentType1,
                fileInputStream1);

        MockMultipartFile image2 = new MockMultipartFile(
                "images",
                fileName2 + "." + contentType2,
                "/testImage/" + contentType2,
                fileInputStream2);

        String writer = "testUser";
        User user = User.builder()
                .account("testUser")
                .password("!test12345")
                .nickname(writer)
                .email("test123@gmail.com")
                .role(Role.USER)
                .build();

        userRepository.save(user);

        BoardCreateServiceRequest boardRequest = BoardCreateServiceRequest.builder()
                .title("축구선수 Goat는?")
                .writer(writer)
                .tag("스포츠")
                .content("메시 vs 호날두")
                .option1("메시")
                .option1Image(image1)
                .option2("호날두")
                .option2Image(image2)
                .build();

        BoardResponse boardResponse = boardService.create(boardRequest,registeredDateTime);

        CommentServiceRequest commentRequest1 = CommentServiceRequest.builder()
                .content("행복합니다")
                .boardId(boardResponse.getId())
                .nickname("testUser")
                .password("1234")
                .build();

        CommentServiceRequest commentRequest2 = CommentServiceRequest.builder()
                .content("행복합니다2")
                .boardId(boardResponse.getId())
                .nickname("testUser2")
                .password("1234")
                .build();

        Long saveComment =commentService.create(commentRequest1);

        //when
        Long result = commentService.update(commentRequest2,saveComment);

        //then
        assertThat(result).isEqualTo(saveComment);
    }

    @DisplayName("수정할 댓글의 비밀번호를 틀리면 에러가 발생한다")
    @Test
    void updateCommentWrongPassword() throws Exception{
        //given
        LocalDateTime registeredDateTime = LocalDateTime.now();
        FileInputStream fileInputStream1 = new FileInputStream(filePath1);
        FileInputStream fileInputStream2 = new FileInputStream(filePath2);
        MockMultipartFile image1 = new MockMultipartFile(
                "images",
                fileName1 + "." + contentType1,
                "/testImage/" + contentType1,
                fileInputStream1);

        MockMultipartFile image2 = new MockMultipartFile(
                "images",
                fileName2 + "." + contentType2,
                "/testImage/" + contentType2,
                fileInputStream2);

        String writer = "testUser";
        User user = User.builder()
                .account("testUser")
                .password("!test12345")
                .nickname(writer)
                .email("test123@gmail.com")
                .role(Role.USER)
                .build();

        userRepository.save(user);

        BoardCreateServiceRequest boardRequest = BoardCreateServiceRequest.builder()
                .title("축구선수 Goat는?")
                .writer(writer)
                .tag("스포츠")
                .content("메시 vs 호날두")
                .option1("메시")
                .option1Image(image1)
                .option2("호날두")
                .option2Image(image2)
                .build();

        BoardResponse boardResponse = boardService.create(boardRequest,registeredDateTime);

        CommentServiceRequest commentRequest1 = CommentServiceRequest.builder()
                .content("행복합니다")
                .boardId(boardResponse.getId())
                .nickname("testUser")
                .password("1234")
                .build();

        CommentServiceRequest commentRequest2 = CommentServiceRequest.builder()
                .content("행복합니다2")
                .boardId(boardResponse.getId())
                .nickname("testUser2")
                .password("12345")
                .build();

        Long saveCommentId = commentService.create(commentRequest1);

        //when
        //then
        assertThatThrownBy(() -> commentService.update(commentRequest2,saveCommentId))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("잘못된 비밀번호 입니다");
    }

    @DisplayName("댓글을 삭제할 수 있다.")
    @Test
    void deleteComment() throws Exception{
        //given
        LocalDateTime registeredDateTime = LocalDateTime.now();
        FileInputStream fileInputStream1 = new FileInputStream(filePath1);
        FileInputStream fileInputStream2 = new FileInputStream(filePath2);
        MockMultipartFile image1 = new MockMultipartFile(
                "images",
                fileName1 + "." + contentType1,
                "/testImage/" + contentType1,
                fileInputStream1);

        MockMultipartFile image2 = new MockMultipartFile(
                "images",
                fileName2 + "." + contentType2,
                "/testImage/" + contentType2,
                fileInputStream2);

        String writer = "testUser";
        User user = User.builder()
                .account("testUser")
                .password("!test12345")
                .nickname(writer)
                .email("test123@gmail.com")
                .role(Role.USER)
                .build();

        userRepository.save(user);

        BoardCreateServiceRequest boardRequest = BoardCreateServiceRequest.builder()
                .title("축구선수 Goat는?")
                .writer(writer)
                .tag("스포츠")
                .content("메시 vs 호날두")
                .option1("메시")
                .option1Image(image1)
                .option2("호날두")
                .option2Image(image2)
                .build();

        BoardResponse boardResponse = boardService.create(boardRequest,registeredDateTime);

        CommentServiceRequest commentRequest1 = CommentServiceRequest.builder()
                .content("행복합니다")
                .boardId(boardResponse.getId())
                .nickname("testUser")
                .password("1234")
                .build();

        Long deleteComment = commentService.create(commentRequest1);

        //when
        Long result = commentService.delete(deleteComment,"1234");

        //then
        assertThat(result).isEqualTo(deleteComment);
    }

    @DisplayName("댓글을 삭제할 때 없는 댓글을 삭제하면 에러가 발생한다")
    @Test
    void deleteCommentNoExist() throws Exception{
        //given
        LocalDateTime registeredDateTime = LocalDateTime.now();
        FileInputStream fileInputStream1 = new FileInputStream(filePath1);
        FileInputStream fileInputStream2 = new FileInputStream(filePath2);
        MockMultipartFile image1 = new MockMultipartFile(
                "images",
                fileName1 + "." + contentType1,
                "/testImage/" + contentType1,
                fileInputStream1);

        MockMultipartFile image2 = new MockMultipartFile(
                "images",
                fileName2 + "." + contentType2,
                "/testImage/" + contentType2,
                fileInputStream2);

        String writer = "testUser";
        User user = User.builder()
                .account("testUser")
                .password("!test12345")
                .nickname(writer)
                .email("test123@gmail.com")
                .role(Role.USER)
                .build();

        userRepository.save(user);

        BoardCreateServiceRequest boardRequest = BoardCreateServiceRequest.builder()
                .title("축구선수 Goat는?")
                .writer(writer)
                .tag("스포츠")
                .content("메시 vs 호날두")
                .option1("메시")
                .option1Image(image1)
                .option2("호날두")
                .option2Image(image2)
                .build();

        BoardResponse boardResponse = boardService.create(boardRequest,registeredDateTime);

        CommentServiceRequest commentRequest1 = CommentServiceRequest.builder()
                .content("행복합니다")
                .boardId(boardResponse.getId())
                .nickname("testUser")
                .password("1234")
                .build();

        Long saveCommentId = commentService.create(commentRequest1);

        //when

        //then
        assertThatThrownBy(() -> commentService.delete(saveCommentId + 1, "12345"))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("존재하지 않는 댓글입니다.");
    }

    @DisplayName("댓글을 삭제할 때 비밀번호를 틀리면 에러가 발생한다")
    @Test
    void deleteCommentWrongPassword() throws Exception{
        //given
        LocalDateTime registeredDateTime = LocalDateTime.now();
        FileInputStream fileInputStream1 = new FileInputStream(filePath1);
        FileInputStream fileInputStream2 = new FileInputStream(filePath2);
        MockMultipartFile image1 = new MockMultipartFile(
                "images",
                fileName1 + "." + contentType1,
                "/testImage/" + contentType1,
                fileInputStream1);

        MockMultipartFile image2 = new MockMultipartFile(
                "images",
                fileName2 + "." + contentType2,
                "/testImage/" + contentType2,
                fileInputStream2);

        String writer = "testUser";
        User user = User.builder()
                .account("testUser")
                .password("!test12345")
                .nickname(writer)
                .email("test123@gmail.com")
                .role(Role.USER)
                .build();

        userRepository.save(user);

        BoardCreateServiceRequest boardRequest = BoardCreateServiceRequest.builder()
                .title("축구선수 Goat는?")
                .writer(writer)
                .tag("스포츠")
                .content("메시 vs 호날두")
                .option1("메시")
                .option1Image(image1)
                .option2("호날두")
                .option2Image(image2)
                .build();

        BoardResponse boardResponse = boardService.create(boardRequest,registeredDateTime);

        CommentServiceRequest commentRequest1 = CommentServiceRequest.builder()
                .content("행복합니다")
                .boardId(boardResponse.getId())
                .nickname("testUser")
                .password("1234")
                .build();

        Long saveCommentId = commentService.create(commentRequest1);

        //when

        //then
        assertThatThrownBy(() -> commentService.delete(saveCommentId, "12345"))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("잘못된 비밀번호 입니다");
    }

    @DisplayName("없는 게시글을 조회하면 에러가 발생한다")
    @Test
    void readCommentNoExistBoard(){
        //given
        LocalDateTime registeredDateTime = LocalDateTime.now();

        //when
        assertThatThrownBy(() -> commentService.findAllCommentByBoardId(1L))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("존재하지 않는 게시글입니다.");

    }
}