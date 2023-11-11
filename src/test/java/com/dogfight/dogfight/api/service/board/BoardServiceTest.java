package com.dogfight.dogfight.api.service.board;

import com.dogfight.dogfight.IntegrationTestSupport;
import com.dogfight.dogfight.api.service.board.request.BoardCreateServiceRequest;
import com.dogfight.dogfight.api.service.user.request.UserRegisterServiceRequest;
import com.dogfight.dogfight.domain.board.BoardRepository;
import com.dogfight.dogfight.domain.tag.TagRepository;
import com.dogfight.dogfight.domain.user.Role;
import com.dogfight.dogfight.domain.user.User;
import com.dogfight.dogfight.domain.user.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.FileInputStream;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class BoardServiceTest extends IntegrationTestSupport {
    @Autowired
    BoardService boardService;
    @Autowired
    BoardRepository boardRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    TagRepository tagRepository;

    final String fileName1 = "messi";
    final String contentType1 = "jpeg";
    final String fileName2 = "ronaldo";
    final String contentType2 = "jpg";
    final String filePath1 = "src/test/resources/testImage/"+fileName1+"."+contentType1;
    final String filePath2 = "src/test/resources/testImage/"+fileName2+"."+contentType2;


    @AfterEach
    void tearDown() {
        boardRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
        tagRepository.deleteAllInBatch();
    }

    @DisplayName("게시판 태그를 저장한다")
    @Test
    void saveBoardTag() throws Exception{
        //given
        LocalDateTime registeredDateTime = LocalDateTime.now();
        FileInputStream fileInputStream1 = new FileInputStream(filePath1);
        FileInputStream fileInputStream2 = new FileInputStream(filePath2);
        MockMultipartFile image1 = new MockMultipartFile(
                "images",
                fileName1 + "." + contentType1,
                contentType1,
                fileInputStream1);

        MockMultipartFile image2 = new MockMultipartFile(
                "images",
                fileName2 + "." + contentType2,
                contentType2,
                fileInputStream2);

        String writer = "tester";
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
        boardService.create(request,registeredDateTime);


        //then

    }

}