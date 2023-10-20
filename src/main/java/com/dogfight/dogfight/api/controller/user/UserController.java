package com.dogfight.dogfight.api.controller.user;

import com.dogfight.dogfight.api.ApiResponse;
import com.dogfight.dogfight.api.controller.user.request.UserRefreshTokenRequest;
import com.dogfight.dogfight.api.controller.user.request.UserRequest;
import com.dogfight.dogfight.api.service.user.UserService;
import com.dogfight.dogfight.api.service.user.request.UserServiceRequest;
import com.dogfight.dogfight.api.service.user.response.UserResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDateTime;

@Controller
@RequestMapping("/api/v0/user")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ApiResponse<UserResponse> register(@RequestBody UserRequest request) {
        LocalDateTime localDateTime = LocalDateTime.now();
        return ApiResponse.ok(
                userService.register(new UserServiceRequest("test","test"), localDateTime)
        );

    }

    @PostMapping("/login")
    public ApiResponse<UserResponse> login(@RequestBody UserRequest request) {
        LocalDateTime localDateTime = LocalDateTime.now();
        return ApiResponse.ok(
                userService.login(new UserServiceRequest("test","test"))
        );

    }

    @PostMapping("/register")
    public ApiResponse<Boolean> logout() {
        LocalDateTime localDateTime = LocalDateTime.now();
        return ApiResponse.ok(
                userService.logout(new UserRefreshTokenRequest("test").toService())
        );

    }

    @PostMapping("/register")
    public ApiResponse<Boolean> withdraw(@RequestBody UserRequest request) {
        LocalDateTime localDateTime = LocalDateTime.now();
        return ApiResponse.ok(
                userService.withdraw(new UserServiceRequest("test","test"), localDateTime)
        );

    }
}
