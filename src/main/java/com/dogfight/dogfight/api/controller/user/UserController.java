package com.dogfight.dogfight.api.controller.user;

import com.dogfight.dogfight.api.ApiResponse;
import com.dogfight.dogfight.api.controller.user.request.UserLoginRequest;
import com.dogfight.dogfight.api.controller.user.request.UserRefreshTokenRequest;
import com.dogfight.dogfight.api.controller.user.request.UserRegisterRequest;
import com.dogfight.dogfight.api.service.user.UserService;
import com.dogfight.dogfight.api.service.user.response.UserResponse;
import com.dogfight.dogfight.api.service.user.response.UserServiceResponse;
import com.dogfight.dogfight.api.service.user.response.UserTokenResponse;
import com.dogfight.dogfight.common.jwt.JwtProvider;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Date;

@RequestMapping("/api/v0/user")
@RestController
@Slf4j
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private JwtProvider jwtProvider;

    @PostMapping("/register")
    public ApiResponse<UserServiceResponse> register(@RequestBody @Valid UserRegisterRequest request) {
        LocalDateTime localDateTime = LocalDateTime.now();
        return ApiResponse.ok(
                userService.register(request.toServiceRequest(), localDateTime)
        );

    }

    @PostMapping("/login")
    public ApiResponse<UserTokenResponse> login(@RequestBody UserLoginRequest request) {
        Date localDateTime = new Date();
        log.info("loginTest");
        return ApiResponse.ok(
                userService.login(request.toServiceRequest(),localDateTime)
        );

    }

    @GetMapping("/logout")
    public ApiResponse<Boolean> logout(HttpServletRequest httpServletRequest) {
        return ApiResponse.ok(
                userService.logout(getAccountByReqBody(httpServletRequest))
        );

    }

    @DeleteMapping
    public ApiResponse<String> withdraw(HttpServletRequest httpServletRequest) {
        return ApiResponse.ok(
                userService.withdraw(getAccountByReqBody(httpServletRequest))
        );

    }

    @PostMapping("/refresh")
    public ApiResponse<UserTokenResponse> refresh(@RequestBody UserRefreshTokenRequest request){
        Date localDateTime = new Date();
        log.info("refreshController = {}", request.getRefreshToken());
        return ApiResponse.ok(
                userService.refresh(request.toService(), localDateTime)
        );
    }

    @GetMapping
    public ApiResponse<UserResponse> getUser(HttpServletRequest httpServletRequest) {

        return ApiResponse.ok(
                userService.getUser(getAccountByReqBody(httpServletRequest))
        );
    }

    public String getAccountByReqBody(HttpServletRequest httpServletRequest) {
        String accessToken = jwtProvider.resolveToken(httpServletRequest);
        return jwtProvider.getAccount(accessToken.replace("Bearer ", ""));
    }
}
