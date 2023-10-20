package com.dogfight.dogfight.api.service.user;

import com.dogfight.dogfight.api.service.user.request.UserServiceRequest;
import com.dogfight.dogfight.api.service.user.response.UserResponse;
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

        UserServiceRequest request = UserServiceRequest.builder()
                .userId("test")
                .password("test")
                .build();

        //when
        UserResponse response = userService.register(request, registeredDateTime);


        //then
        System.out.println(response.getId());
    }
}