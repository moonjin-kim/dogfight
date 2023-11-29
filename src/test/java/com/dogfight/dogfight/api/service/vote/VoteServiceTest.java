package com.dogfight.dogfight.api.service.vote;

import com.dogfight.dogfight.api.service.board.BoardServiceImpl;
import com.dogfight.dogfight.api.service.board.request.BoardCreateServiceRequest;
import com.dogfight.dogfight.api.service.board.response.BoardResponse;
import com.dogfight.dogfight.api.service.vote.response.VoteResponse;
import com.dogfight.dogfight.domain.board.BoardRepository;
import com.dogfight.dogfight.domain.tag.TagRepository;
import com.dogfight.dogfight.domain.user.Role;
import com.dogfight.dogfight.domain.user.User;
import com.dogfight.dogfight.domain.user.UserRepository;
import com.dogfight.dogfight.domain.vote.VoteRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;

import java.io.FileInputStream;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
class VoteServiceTest {

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
    VoteServiceImpl voteService;

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
    }

    @DisplayName("1번을 투표하면 option1count의 숫자가 1 증가한다.")
    @Test
    void voteFirstOption() throws Exception{
        //given
        LocalDateTime registeredDateTime = LocalDateTime.now();
        FileInputStream fileInputStream1 = new FileInputStream(filePath1);
        FileInputStream fileInputStream2 = new FileInputStream(filePath2);
        MockMultipartFile image1 = new MockMultipartFile(
                "images",
                fileName1 + "." + contentType1,
                "/image/" + contentType1,
                fileInputStream1);

        MockMultipartFile image2 = new MockMultipartFile(
                "images",
                fileName2 + "." + contentType2,
                "/image/" + contentType2,
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

        long id = boardResponse.getVote().getId();

        VoteResponse result = voteService.vote(id,1);

        assertThat(result.getOption1Count()).isEqualTo(1);

        VoteResponse result2 = voteService.vote(id,1);

        assertThat(result2.getOption1Count()).isEqualTo(2);

    }

    @DisplayName("2번을 투표하면 option2count의 숫자가 1 증가한다.")
    @Test
    void voteSecondOption() throws Exception{
        //given
        LocalDateTime registeredDateTime = LocalDateTime.now();
        FileInputStream fileInputStream1 = new FileInputStream(filePath1);
        FileInputStream fileInputStream2 = new FileInputStream(filePath2);
        MockMultipartFile image1 = new MockMultipartFile(
                "images",
                fileName1 + "." + contentType1,
                "/image/" + contentType1,
                fileInputStream1);

        MockMultipartFile image2 = new MockMultipartFile(
                "images",
                fileName2 + "." + contentType2,
                "/image/" + contentType2,
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

        long id = boardResponse.getVote().getId();

        VoteResponse result = voteService.vote(id,2);

        assertThat(result.getOption2Count()).isEqualTo(1);

        VoteResponse result2 = voteService.vote(id,2);

        assertThat(result2.getOption2Count()).isEqualTo(2);

    }

    @DisplayName("3번을 투표하면 예외를 발생한다.")
    @Test
    void vote3Option() throws Exception{
        //given
        LocalDateTime registeredDateTime = LocalDateTime.now();
        FileInputStream fileInputStream1 = new FileInputStream(filePath1);
        FileInputStream fileInputStream2 = new FileInputStream(filePath2);
        MockMultipartFile image1 = new MockMultipartFile(
                "images",
                fileName1 + "." + contentType1,
                "/image/" + contentType1,
                fileInputStream1);

        MockMultipartFile image2 = new MockMultipartFile(
                "images",
                fileName2 + "." + contentType2,
                "/image/" + contentType2,
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

        long id = boardResponse.getVote().getId();

        assertThatThrownBy(() -> voteService.vote(id,3))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("잘못된 투표 번호 입니다.");

    }
}