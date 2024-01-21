package com.dogfight.dogfight.api.service.board;

import com.dogfight.dogfight.IntegrationTestSupport;
import com.dogfight.dogfight.api.service.board.request.BoardCreateServiceRequest;
import com.dogfight.dogfight.api.service.board.request.BoardUpdateServiceRequest;
import com.dogfight.dogfight.api.service.board.response.BoardResponse;
import com.dogfight.dogfight.common.filehandler.FileHandler;
import com.dogfight.dogfight.config.error.CustomException;
import com.dogfight.dogfight.domain.board.Board;
import com.dogfight.dogfight.domain.board.BoardRepository;
import com.dogfight.dogfight.domain.tag.Tag;
import com.dogfight.dogfight.domain.tag.TagRepository;
import com.dogfight.dogfight.domain.user.Role;
import com.dogfight.dogfight.domain.user.User;
import com.dogfight.dogfight.domain.user.UserRepository;
import com.dogfight.dogfight.domain.vote.Vote;
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
class BoardServiceTest extends IntegrationTestSupport {
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
    FileHandler fileHandler;

    final String fileName1 = "messi";
    final String contentType1 = "jpeg";
    final String fileName2 = "ronaldo";
    final String contentType2 = "jpeg";
    final String filePath1 = "src/test/resources/testImage/"+fileName1+"."+contentType1;
    final String filePath2 = "src/test/resources/testImage/"+fileName2+"."+contentType2;


    @AfterEach
    void tearDown() {
        boardRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
        tagRepository.deleteAllInBatch();
        voteRepository.deleteAllInBatch();
        tagRepository.deleteAllInBatch();
        fileHandler.deleteFolder();
    }

    @DisplayName("게시판을 저장한다")
    @Test
    void saveBoardTag() throws Exception{
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

        BoardCreateServiceRequest request = BoardCreateServiceRequest.builder()
                .title("축구선수 Goat는?")
                .writer(writer)
                .tag("스포츠")
                .content("메시 vs 호날두")
                .option1("메시")
                .option1Image(image1)
                .option2("호날두")
                .option2Image(image2)
                .build();

        //when
        BoardResponse boardResponse = boardService.create(request,registeredDateTime);

        //then
        assertThat(boardResponse.getId()).isNotNull();

        assertThat(boardResponse)
                .extracting("title","writer","tag","content")
                .contains("축구선수 Goat는?",writer,"스포츠","메시 vs 호날두");

        List<Board> boards = boardRepository.findAll();
        assertThat(boards).hasSize(1);

        List<Tag> tags = tagRepository.findAll();
        assertThat(tags).hasSize(1);

        List<Vote> votes = voteRepository.findAll();
        assertThat(votes).hasSize(1);

    }

    @DisplayName("게시판을 등록할 때 이미 태그가 존재하면 태그를 새로 생성하지 않고 게시판을 저장한다")
    @Test
    void createBoardFindTagNoAddTag() throws Exception{
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

        String tagName = "스포츠";

        BoardCreateServiceRequest request = BoardCreateServiceRequest.builder()
                .title("축구선수 Goat는?")
                .writer(writer)
                .tag(tagName)
                .content("메시 vs 호날두")
                .option1("메시")
                .option1Image(image1)
                .option2("호날두")
                .option2Image(image2)
                .build();

        Tag tag = Tag.builder()
                .name(tagName)
                .build();

        Tag saveTag = tagRepository.save(tag);

        //when
        BoardResponse boardResponse = boardService.create(request,registeredDateTime);

        //then
        assertThat(boardResponse.getId()).isNotNull();

        assertThat(boardResponse)
                .extracting("title","writer","tag","content")
                .contains("축구선수 Goat는?",writer,"스포츠","메시 vs 호날두");

        List<Board> boards = boardRepository.findAll();
        assertThat(boards).hasSize(1);
        assertThat(boards.get(0).getTag().getId()).isEqualTo(saveTag.getId());

        List<Tag> tags = tagRepository.findAll();
        assertThat(tags).hasSize(1);

        List<Vote> votes = voteRepository.findAll();
        assertThat(votes).hasSize(1);
    }

    @DisplayName("등록된 게시글을 id로 조회할 수 있다.")
    @Test
    void readBoard() throws Exception {
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

        String tagName = "스포츠";

        BoardCreateServiceRequest request = BoardCreateServiceRequest.builder()
                .title("축구선수 Goat는?")
                .writer(writer)
                .tag(tagName)
                .content("메시 vs 호날두")
                .option1("메시")
                .option1Image(image1)
                .option2("호날두")
                .option2Image(image2)
                .build();

        Tag tag = Tag.builder()
                .name(tagName)
                .build();

        Tag saveTag = tagRepository.save(tag);
        BoardResponse saveBoardResponse = boardService.create(request,registeredDateTime);

        //when
        BoardResponse readBoardResponse1 = boardService.read(saveBoardResponse.getId());

        //then
        assertThat(readBoardResponse1.getId()).isEqualTo(saveBoardResponse.getId());
    }

    @DisplayName("등록된 게시글을 id로 조회할 수 있다.")
    @Test
    void readBoardToIncreaseViews() throws Exception {
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

        String tagName = "스포츠";

        BoardCreateServiceRequest request = BoardCreateServiceRequest.builder()
                .title("축구선수 Goat는?")
                .writer(writer)
                .tag(tagName)
                .content("메시 vs 호날두")
                .option1("메시")
                .option1Image(image1)
                .option2("호날두")
                .option2Image(image2)
                .build();

        Tag tag = Tag.builder()
                .name(tagName)
                .build();

        Tag saveTag = tagRepository.save(tag);
        BoardResponse saveBoardResponse = boardService.create(request,registeredDateTime);

        //when
        BoardResponse readBoardResponse1 = boardService.read(saveBoardResponse.getId());

        //then
        assertThat(readBoardResponse1.getId()).isEqualTo(saveBoardResponse.getId());
        assertThat(readBoardResponse1.getViews()).isEqualTo(1);
    }

    @DisplayName("등록된 게시글을 수정 가능하다.")
    @Test
    void updateBoard() throws Exception {
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

        String tagName = "스포츠";

        BoardCreateServiceRequest request = BoardCreateServiceRequest.builder()
                .title("축구선수 Goat는?")
                .writer(writer)
                .tag(tagName)
                .content("메시 vs 호날두")
                .option1("메시")
                .option1Image(image1)
                .option2("호날두")
                .option2Image(image2)
                .build();

        Tag tag = Tag.builder()
                .name(tagName)
                .build();

        Tag saveTag = tagRepository.save(tag);
        BoardResponse saveBoardResponse = boardService.create(request,registeredDateTime);
        Long voteId = saveBoardResponse.getVote().getId();

        BoardUpdateServiceRequest updateRequest = BoardUpdateServiceRequest.builder()
                .id(saveBoardResponse.getId())
                .title("축구선수 Goat는2?")
                .writer(user.getAccount())
                .tag(tagName)
                .content("메시 vs 호날두2")
                .voteId(voteId)
                .option1("메시")
                .option1Image(image1)
                .option2("호날두")
                .option2Image(image2)
                .build();
        //when
        BoardResponse updateBoardResponse = boardService.update(updateRequest);

        //then
        assertThat(updateBoardResponse.getId()).isEqualTo(saveBoardResponse.getId());
        assertThat(updateBoardResponse)
                .extracting("title","writer","tag","content")
                .contains("축구선수 Goat는2?",writer,"스포츠","메시 vs 호날두2");
    }

    @DisplayName("등록되지 않은 게시글 id로 조회하면 예외가 발생한다.")
    @Test
    void readBoardWithOutId() throws Exception {
        //then
        assertThatThrownBy(() -> boardService.read(1L))
                .isInstanceOf(CustomException.class)
                .hasMessage("존재하지 않는 게시글입니다.");

    }

    @DisplayName("등록된 게시글을 id로 삭제할 수 있다.")
    @Test
    void deleteBoard() throws Exception {
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

        String tagName = "스포츠";

        BoardCreateServiceRequest request = BoardCreateServiceRequest.builder()
                .title("축구선수 Goat는?")
                .writer(writer)
                .tag(tagName)
                .content("메시 vs 호날두")
                .option1("메시")
                .option1Image(image1)
                .option2("호날두")
                .option2Image(image2)
                .build();

        Tag tag = Tag.builder()
                .name(tagName)
                .build();

        tagRepository.save(tag);
        BoardResponse saveBoardResponse = boardService.create(request,registeredDateTime);
        boardService.delete(saveBoardResponse.getId());

        //when
        assertThatThrownBy(() -> boardService.read(saveBoardResponse.getId()))
                .isInstanceOf(CustomException.class)
                .hasMessage("존재하지 않는 게시글입니다.");

    }

    @DisplayName("게시판을 페이지 형식으로 조회 가능하다")
    @Test
    void getListBoard() throws Exception{
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

        BoardCreateServiceRequest request1 = BoardCreateServiceRequest.builder()
                .title("축구선수 Goat는3?")
                .writer(writer)
                .tag("스포츠")
                .content("메시 vs 호날두")
                .option1("메시")
                .option1Image(image1)
                .option2("호날두")
                .option2Image(image2)
                .build();

        BoardCreateServiceRequest request2 = BoardCreateServiceRequest.builder()
                .title("축구선수 Goat는2?")
                .writer(writer)
                .tag("스포츠")
                .content("메시 vs 호날두")
                .option1("메시")
                .option1Image(image1)
                .option2("호날두")
                .option2Image(image2)
                .build();

        BoardCreateServiceRequest request3 = BoardCreateServiceRequest.builder()
                .title("축구선수 Goat는1?")
                .writer(writer)
                .tag("스포츠")
                .content("메시 vs 호날두")
                .option1("메시")
                .option1Image(image1)
                .option2("호날두")
                .option2Image(image2)
                .build();

        //when
        BoardResponse boardResponse1 = boardService.create(request1,registeredDateTime);
        boardService.create(request2,registeredDateTime);
        boardService.create(request2,registeredDateTime);

        //then
        assertThat(boardResponse1.getId()).isNotNull();

        assertThat(boardResponse1)
                .extracting("title","writer","tag","content")
                .contains("축구선수 Goat는3?",writer,"스포츠","메시 vs 호날두");


        List<Board> boards = boardRepository.findAll();
        assertThat(boards).hasSize(3);

        List<Tag> tags = tagRepository.findAll();
        assertThat(tags).hasSize(1);

        List<Vote> votes = voteRepository.findAll();
        assertThat(votes).hasSize(3);

    }
}