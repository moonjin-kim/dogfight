package com.dogfight.dogfight.api.service.user;

import com.dogfight.dogfight.api.service.user.request.UserServiceRefreshTokenRequest;
import com.dogfight.dogfight.api.service.user.request.UserRegisterServiceRequest;
import com.dogfight.dogfight.api.service.user.response.UserResponse;
import com.dogfight.dogfight.api.service.user.response.UserTokenResponse;
import com.dogfight.dogfight.domain.user.User;
import com.dogfight.dogfight.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Log4j2
@Service
public class UserServiceImpl implements  UserService{
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    @Override
    @Transactional
    public UserResponse register(UserRegisterServiceRequest userLoginServiceRequest, LocalDateTime registerDateTime) {
        User user = userLoginServiceRequest.toEntity();

        User savedUser = userRepository.save(user);


        return UserResponse.of(savedUser);
    }

    @Override
    @Transactional
    public UserTokenResponse login(UserRegisterServiceRequest userLoginServiceRequest) {
        try {
//            Authentication authentication = authenticationManager.authenticate(
//                    new UsernamePasswordAuthenticationToken(
//                            userLoginServiceRequest.getAccount(),
//                            userLoginServiceRequest.getPassword()
//                    )
//            );

            UserTokenResponse userTokenResponse = UserTokenResponse.builder()
                    .requestToken("test")
                    .accessToken("test")
                    .build();

            return userTokenResponse;
        } catch(BadCredentialsException e){
            log.error("아이디 혹은 패스워드가 잘못되었습니다");
            throw new BadCredentialsException("아이디 혹은 패스워드가 잘못되었습니다");
        }
    }

    @Override
    public boolean logout(UserServiceRefreshTokenRequest request) {
        return false;
    }

    @Override
    public boolean withdraw(UserRegisterServiceRequest userLoginServiceRequest, LocalDateTime registerDateTime) {
        return false;
    }
}
