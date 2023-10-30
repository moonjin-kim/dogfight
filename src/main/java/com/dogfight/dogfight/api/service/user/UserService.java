package com.dogfight.dogfight.api.service.user;

import com.dogfight.dogfight.api.service.user.request.UserRegisterServiceRequest;
import com.dogfight.dogfight.api.service.user.request.UserServiceRefreshTokenRequest;
import com.dogfight.dogfight.api.service.user.response.UserResponse;
import com.dogfight.dogfight.api.service.user.response.UserTokenResponse;

import java.time.LocalDateTime;
import java.util.Date;

public interface UserService {

    public UserResponse register(UserRegisterServiceRequest userLoginServiceRequest, LocalDateTime registerDateTime);

    public UserTokenResponse login(UserRegisterServiceRequest userLoginServiceRequest, Date date);

    public boolean logout(UserServiceRefreshTokenRequest request);

    public String withdraw(UserServiceRefreshTokenRequest request);

    public UserTokenResponse refresh(UserServiceRefreshTokenRequest userServiceRefreshTokenRequest,Date date);

    public boolean ConfirmationPassword(String password);


}
