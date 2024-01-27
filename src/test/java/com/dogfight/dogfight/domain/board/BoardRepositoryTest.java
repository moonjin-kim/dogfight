package com.dogfight.dogfight.domain.board;

import com.dogfight.dogfight.IntegrationTestSupport;
import com.dogfight.dogfight.api.service.board.BoardServiceImpl;
import com.dogfight.dogfight.api.service.board.request.BoardCreateServiceRequest;
import com.dogfight.dogfight.api.service.board.response.BoardResponse;
import com.dogfight.dogfight.common.filehandler.FileHandler;
import com.dogfight.dogfight.domain.tag.TagRepository;
import com.dogfight.dogfight.domain.user.Role;
import com.dogfight.dogfight.domain.user.User;
import com.dogfight.dogfight.domain.user.UserRepository;
import com.dogfight.dogfight.domain.vote.VoteRepository;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.mock.web.MockMultipartFile;

import java.io.FileInputStream;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;


@Slf4j
class BoardRepositoryTest  extends IntegrationTestSupport {

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

    User initUser(String writer) throws Exception {

        User user = User.builder()
                .account("testUser")
                .password("!test12345")
                .nickname(writer)
                .email("test123@gmail.com")
                .role(Role.USER)
                .build();

        return userRepository.save(user);
    }
    BoardCreateServiceRequest saveBoard(String writer, MockMultipartFile image1, MockMultipartFile image2) {
        return BoardCreateServiceRequest.builder()
                .title("축구선수 Goat는2?")
                .writer(writer)
                .tag("스포츠")
                .content("메시 vs 호날두")
                .option1("메시")
                .option1Image(image1)
                .option2("호날두")
                .option2Image(image2)
                .build();
    }


    @DisplayName("게시판을 페이지 형식으로 조회 가능하다")
    @Test
    void searchBoardPage() throws Exception{
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

        User userResult = initUser(writer);

        BoardCreateServiceRequest request1 = saveBoard(writer,image1,image2);
        BoardCreateServiceRequest request2 = saveBoard(writer,image1,image2);
        BoardCreateServiceRequest request3 = saveBoard(writer,image1,image2);

        //when
        BoardResponse boardResponse1 = boardService.create(request1,registeredDateTime);
        BoardResponse boardResponse2 = boardService.create(request2,registeredDateTime);
        BoardResponse boardResponse3 = boardService.create(request3,registeredDateTime);


        //then
        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "views"));
        Page<Board> boards = boardRepository.search(pageable,null,null);
        Assertions.assertThat(boards.getSize()).isEqualTo(3);


    }

    @DisplayName("게시판을 tag로 검색 가능하다.")
    @Test
    void searchBoardByTag() throws Exception{
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

        User userResult = initUser(writer);

        BoardCreateServiceRequest request1 = saveBoard(writer,image1,image2);
        BoardCreateServiceRequest request2 = saveBoard(writer,image1,image2);
        BoardCreateServiceRequest request3 = saveBoard(writer,image1,image2);

        //when
        BoardResponse boardResponse1 = boardService.create(request1,registeredDateTime);
        BoardResponse boardResponse2 = boardService.create(request2,registeredDateTime);
        BoardResponse boardResponse3 = boardService.create(request3,registeredDateTime);


        //then
        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "views"));
        Page<Board> boards = boardRepository.search(pageable,null,"인물");
        Assertions.assertThat(boards.getSize()).isEqualTo(1);

        Page<Board> board1 = boardRepository.search(pageable,null,"스포츠");
        Assertions.assertThat(board1.getSize()).isEqualTo(2);

    }

}