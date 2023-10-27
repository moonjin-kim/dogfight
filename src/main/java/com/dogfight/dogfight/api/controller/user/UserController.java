package com.dogfight.dogfight.api.controller.user;

import com.dogfight.dogfight.api.ApiResponse;
import com.dogfight.dogfight.api.controller.user.request.UserLoginRequest;
import com.dogfight.dogfight.api.controller.user.request.UserRefreshTokenRequest;
import com.dogfight.dogfight.api.controller.user.request.UserRegisterRequest;
import com.dogfight.dogfight.api.service.user.UserService;
import com.dogfight.dogfight.api.service.user.response.UserResponse;
import com.dogfight.dogfight.api.service.user.response.UserTokenResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RequestMapping("/api/v0/user")
@RestController
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ApiResponse<UserResponse> register(@RequestBody UserRegisterRequest request) {
        LocalDateTime localDateTime = LocalDateTime.now();
        return ApiResponse.ok(
                userService.register(request.toServiceRequest(), localDateTime)
        );

    }

    @PostMapping("/login")
    public ApiResponse<UserTokenResponse> login(@RequestBody UserLoginRequest request) {
        LocalDateTime localDateTime = LocalDateTime.now();
        System.out.println("loginTest");
        return ApiResponse.ok(
                userService.login(request.toServiceRequest())
        );

    }

    @PostMapping("/logout")
    public ApiResponse<Boolean> logout() {
        LocalDateTime localDateTime = LocalDateTime.now();
        return ApiResponse.ok(
                userService.logout(new UserRefreshTokenRequest("test").toService())
        );

    }

    @PostMapping("/withdraw")
    public ApiResponse<Boolean> withdraw(@RequestBody UserRegisterRequest request) {
        LocalDateTime localDateTime = LocalDateTime.now();
        return ApiResponse.ok(
                userService.withdraw(request.toServiceRequest(), localDateTime)
        );

    }

    @GetMapping("/test")
    public ApiResponse<String> test(){
        System.out.println("test");
        return ApiResponse.ok(
                "test"
        );
    }
}
