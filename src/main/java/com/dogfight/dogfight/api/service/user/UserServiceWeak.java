package com.dogfight.dogfight.api.service.user;

import com.dogfight.dogfight.api.service.user.request.UserServiceRequest;
import com.dogfight.dogfight.api.service.user.request.UserServiceRefreshTokenRequest;
import com.dogfight.dogfight.api.service.user.response.UserResponse;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UserServiceWeak implements UserService {
    @Override
    public UserResponse register(UserServiceRequest userLoginServiceRequest, LocalDateTime registerDateTime) {
        return new UserResponse(1L, "test");
    }

    @Override
    public UserResponse login(UserServiceRequest userLoginServiceRequest) {
        return new UserResponse(1L, "test");
    }

    @Override
    public boolean logout(UserServiceRefreshTokenRequest request) {
        return true;
    }

    @Override
    public boolean withdraw(UserServiceRequest userLoginServiceRequest, LocalDateTime registerDateTime) {
        return true;
    }
}
