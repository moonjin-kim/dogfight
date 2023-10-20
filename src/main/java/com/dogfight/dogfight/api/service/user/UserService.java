package com.dogfight.dogfight.api.service.user;

import com.dogfight.dogfight.api.service.user.request.UserServiceRequest;
import com.dogfight.dogfight.api.service.user.request.UserServiceRefreshTokenRequest;
import com.dogfight.dogfight.api.service.user.response.UserResponse;

import java.time.LocalDateTime;

public interface UserService {

    public UserResponse register(UserServiceRequest userLoginServiceRequest, LocalDateTime registerDateTime);

    public UserResponse login(UserServiceRequest userLoginServiceRequest);

    public boolean logout(UserServiceRefreshTokenRequest request);

    public boolean withdraw(UserServiceRequest userLoginServiceRequest, LocalDateTime registerDateTime);



}
