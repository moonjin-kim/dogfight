package com.dogfight.dogfight.api.service.user;

import com.dogfight.dogfight.api.service.user.request.UserRegisterServiceRequest;
import com.dogfight.dogfight.api.service.user.response.UserServiceResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

@SpringBootTest
class UserServiceWeakTest {

    @Autowired
    private UserService userService;

    @DisplayName("아이디, 비밀번호를 입력하면 회원가입이 된다.")
    @Test
    void register(){
        //given
        LocalDateTime registeredDateTime = LocalDateTime.now();

        UserRegisterServiceRequest request = UserRegisterServiceRequest.builder()
                .account("test")
                .password("test")
                .nickname("testUser")
                .email("test@test.com")
                .build();

        //when
        UserServiceResponse response = userService.register(request, registeredDateTime);


        //then
        System.out.println(response.getId());
    }
}