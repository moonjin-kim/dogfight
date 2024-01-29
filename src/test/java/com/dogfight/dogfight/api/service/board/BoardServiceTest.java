package com.dogfight.dogfight.api.service.board;

import com.dogfight.dogfight.IntegrationTestSupport;
import com.dogfight.dogfight.api.service.board.request.BoardCreateServiceRequest;
import com.dogfight.dogfight.api.service.board.request.BoardUpdateServiceRequest;
import com.dogfight.dogfight.api.service.board.response.BoardResponse;
import com.dogfight.dogfight.common.filehandler.FileHandler;
import com.dogfight.dogfight.config.error.CustomException;
import com.dogfight.dogfight.config.error.ErrorCode;
import com.dogfight.dogfight.domain.board.Board;
import com.dogfight.dogfight.domain.board.BoardRepository;
import com.dogfight.dogfight.domain.tag.Tag;
import com.dogfight.dogfight.domain.tag.TagRepository;
import com.dogfight.dogfight.domain.tag.response.TagListResponse;
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

    User initUser(String writer) {
       User user = User.builder()
                .account("testUser")
                .password("!test12345")
                .nickname(writer)
                .email("test123@gmail.com")
                .role(Role.USER)
                .build();
        return userRepository.save(user);
    }

    BoardCreateServiceRequest initBoard(String writer,String title , String tag,MockMultipartFile image1,MockMultipartFile image2,LocalDateTime registeredDateTime) {
        return BoardCreateServiceRequest.builder()
                .title(title)
                .writer(writer)
                .tag(tag)
                .content("메시 vs 호날두")
                .option1("메시")
                .option1Image(image1)
                .option2("호날두")
                .option2Image(image2)
                .build();

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
        User user = initUser(writer);

        String title = "축구선수 Goat는?";
        String tag = "스포츠";

        BoardCreateServiceRequest request = initBoard(writer,title,tag ,image1, image2,registeredDateTime);

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

    @DisplayName("존재하지 않는 유저가 게시판을 생성하면 에러가 발생한다.")
    @Test
    void saveBoardNoExistUser() throws Exception{
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

        String title = "축구선수 Goat는?";
        String tag = "스포츠";

        BoardCreateServiceRequest request = initBoard(writer,title,tag, image1, image2,registeredDateTime);
        //when
        assertThatThrownBy(() -> boardService.create(request,registeredDateTime))
                .isInstanceOf(CustomException.class)
                .hasMessage("존재하지 않는 유저입니다.");

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
        initUser(writer);

        String title = "축구선수 Goat는?";
        String tag = "스포츠";

        BoardCreateServiceRequest request = initBoard(writer,title,tag ,image1, image2,registeredDateTime);
        BoardResponse boardResponse = boardService.create(request,registeredDateTime);

        //when
        BoardResponse readBoardResponse1 = boardService.read(boardResponse.getId());

        //then
        assertThat(readBoardResponse1.getId()).isEqualTo(boardResponse.getId());
    }

    @DisplayName("게시글을 조회할 때 views가 1씩 증가한다.")
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
        initUser(writer);


        String title = "축구선수 Goat는?";
        String tag = "스포츠";

        BoardCreateServiceRequest request = initBoard(writer,title,tag ,image1, image2,registeredDateTime);
        BoardResponse boardResponse = boardService.create(request,registeredDateTime);

        //when
        BoardResponse readBoardResponse1 = boardService.read(boardResponse.getId());

        //then
        assertThat(readBoardResponse1.getId()).isEqualTo(boardResponse.getId());
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
        User user = initUser(writer);


        String title = "축구선수 Goat는?";
        String tag = "스포츠";

        BoardCreateServiceRequest request = initBoard(writer,title,tag ,image1, image2,registeredDateTime);
        BoardResponse boardResponse = boardService.create(request,registeredDateTime);
        Long voteId = boardResponse.getVote().getId();

        BoardUpdateServiceRequest updateRequest = BoardUpdateServiceRequest.builder()
                .id(boardResponse.getId())
                .title("축구선수 Goat는2?")
                .writer(user.getAccount())
                .tag(tag)
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
        assertThat(updateBoardResponse.getId()).isEqualTo(boardResponse.getId());
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
        User user = initUser(writer);

        String title = "축구선수 Goat는?";
        String tag = "스포츠";

        BoardCreateServiceRequest request = initBoard(writer,title,tag ,image1, image2,registeredDateTime);

        //when
        BoardResponse boardResponse = boardService.create(request,registeredDateTime);
        boardService.delete(boardResponse.getId());

        //when
        assertThatThrownBy(() -> boardService.read(boardResponse.getId()))
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
        User user = initUser(writer);

        String title = "축구선수 Goat는?";
        String tag = "스포츠";

        BoardCreateServiceRequest request1 = initBoard("축구선수 Goat는?",title,tag ,image1, image2,registeredDateTime);
        BoardCreateServiceRequest request2 = initBoard("축구선수 Goat는1?",title,tag ,image1, image2,registeredDateTime);
        BoardCreateServiceRequest request3 = initBoard("축구선수 Goat는2?",title,tag ,image1, image2,registeredDateTime);

        //when
        BoardResponse boardResponse1 = boardService.create(request1,registeredDateTime);
        boardService.create(request2,registeredDateTime);
        boardService.create(request3,registeredDateTime);

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

    @DisplayName("태그들을 조회 가능하다")
    @Test
    void getTags() throws Exception{
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
        User user = initUser(writer);

        String title = "축구선수 Goat는?";
        String tag = "스포츠";

        BoardCreateServiceRequest request1 = initBoard(writer,title,"스포츠", image1, image2,registeredDateTime);
        BoardCreateServiceRequest request2 = initBoard(writer,title,"게임", image1, image2,registeredDateTime);
        BoardCreateServiceRequest request3 = initBoard(writer,title,"인물", image1, image2,registeredDateTime);

        boardService.create(request1,registeredDateTime);
        boardService.create(request2,registeredDateTime);
        boardService.create(request3,registeredDateTime);


        //when
        List<TagListResponse> tags = boardService.getTag();

        //then
        log.info("tagLists = {}" ,tags.get(0).getName());
        assertThat(tags).hasSize(4);
    }

    @DisplayName("태그별 게시글 개수를 조회 가능하다.")
    @Test
    void getTagsByBoardsCount() throws Exception{
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
        User user = initUser(writer);

        String title = "축구선수 Goat는?";
        String tag = "스포츠";

        BoardCreateServiceRequest request1 = initBoard(writer,title,"하이볼", image1, image2,registeredDateTime);
        BoardCreateServiceRequest request2 = initBoard(writer,title,"게임", image1, image2,registeredDateTime);
        BoardCreateServiceRequest request3 = initBoard(writer,title,"인물", image1, image2,registeredDateTime);
        BoardCreateServiceRequest request4 = initBoard(writer,title,"인물", image1, image2,registeredDateTime);

        boardService.create(request1,registeredDateTime);
        boardService.create(request2,registeredDateTime);
        boardService.create(request3,registeredDateTime);
        boardService.create(request4,registeredDateTime);


        //when
        List<TagListResponse> tags = boardService.getTag();

        //then
        assertThat(tags).hasSize(4);
        assertThat(tags.get(0).getName()).isEqualTo("게임");
        assertThat(tags.get(0).getCount()).isEqualTo(1);

        assertThat(tags.get(1).getName()).isEqualTo("인물");
        assertThat(tags.get(1).getCount()).isEqualTo(2);

        assertThat(tags.get(2).getName()).isEqualTo("하이볼");
        assertThat(tags.get(2).getCount()).isEqualTo(1);

        assertThat(tags.get(3).getName()).isEqualTo("전체");
        assertThat(tags.get(3).getCount()).isEqualTo(4);
    }
}