package com.dogfight.dogfight.api.service.user;

import com.dogfight.dogfight.api.service.user.request.UserRegisterServiceRequest;
import com.dogfight.dogfight.api.service.user.request.UserServiceRefreshTokenRequest;
import com.dogfight.dogfight.api.service.user.response.UserResponse;
import com.dogfight.dogfight.api.service.user.response.UserTokenResponse;

import java.time.LocalDateTime;

public interface UserService {

    public UserResponse register(UserRegisterServiceRequest userLoginServiceRequest, LocalDateTime registerDateTime);

    public UserTokenResponse login(UserRegisterServiceRequest userLoginServiceRequest);

    public boolean logout(UserServiceRefreshTokenRequest request);

    public boolean withdraw(UserRegisterServiceRequest userLoginServiceRequest, LocalDateTime registerDateTime);



}
