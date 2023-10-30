package com.dogfight.dogfight.api.service.user;

import com.dogfight.dogfight.api.service.user.request.UserLoginServiceRequest;
import com.dogfight.dogfight.api.service.user.request.UserRegisterServiceRequest;
import com.dogfight.dogfight.api.service.user.response.UserResponse;
import com.dogfight.dogfight.domain.user.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
class UserServiceImplTest{
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;

    @AfterEach
    void tearDown(){
        userRepository.deleteAllInBatch();
    }

    @DisplayName("비밀번호는 영어 소/대문자, 특수문자, 숫자를 포함하여 8자 이상이다.")
    @Test
    void validPassWord(){
        //given
        String password = "!test123";

        //when /then
        assertThat(userService.ConfirmationPassword(password)).isTrue();
    }

    @DisplayName("비밀번호는 영어 소/대문자, 특수문자, 숫자를 포함하여 8자 이상이다.")
    @Test
    void notValidPassWord(){
        //given
        String password = "!test12";

        //when /then
        assertThat(userService.ConfirmationPassword(password)).isFalse();
    }

    @DisplayName("아이디, 비밀번호, 이메일, 닉네임을 모두 입력하면 회원가입 가능하다.")
    @Test
    void register(){
        LocalDateTime now = LocalDateTime.now();
        //given
        UserRegisterServiceRequest userRegisterServiceRequest = UserRegisterServiceRequest.builder()
                .account("test")
                .password("!test123")
                .email("test@naver.com")
                .nickname("testuser")
                .build();

        //when
        UserResponse userResponse = userService.register(userRegisterServiceRequest, now);

        //then
        assertThat(userResponse.getUserId()).isEqualTo("test");

    }

    @DisplayName("중복되는 아이디로 회원가입 시 예외가 발생한다.")
    @Test
    void registerDupId(){
        LocalDateTime registerDateTime = LocalDateTime.now();
        //given
        UserRegisterServiceRequest userRegisterServiceRequest = UserRegisterServiceRequest.builder()
                .account("test")
                .password("!test123")
                .email("test@naver.com")
                .nickname("testuser")
                .build();

        //when
        UserResponse userResponse = userService.register(userRegisterServiceRequest, registerDateTime);

        assertThatThrownBy(() ->userService.register(userRegisterServiceRequest, registerDateTime))
                .isInstanceOf(DataIntegrityViolationException.class);

    }

}