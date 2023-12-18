package com.dogfight.dogfight.api.service.user;

import com.dogfight.dogfight.api.service.user.request.UserRegisterServiceRequest;
import com.dogfight.dogfight.api.service.user.request.UserServiceRefreshTokenRequest;
import com.dogfight.dogfight.api.service.user.response.UserResponse;
import com.dogfight.dogfight.api.service.user.response.UserServiceResponse;
import com.dogfight.dogfight.api.service.user.response.UserTokenResponse;
import jakarta.servlet.http.HttpServletRequest;

import java.time.LocalDateTime;
import java.util.Date;

public interface UserService {

    public UserServiceResponse register(UserRegisterServiceRequest userLoginServiceRequest, LocalDateTime registerDateTime);

    public UserTokenResponse login(UserRegisterServiceRequest userLoginServiceRequest, Date date);

    public boolean logout(String account);

    public String withdraw(String account);

    public UserTokenResponse refresh(UserServiceRefreshTokenRequest userServiceRefreshTokenRequest,Date date);

    public boolean ConfirmationPassword(String pwd);

    public UserResponse getUser(String account);

}
