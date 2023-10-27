package com.dogfight.dogfight.api.service.user;

import com.dogfight.dogfight.api.service.user.request.UserServiceRefreshTokenRequest;
import com.dogfight.dogfight.api.service.user.request.UserRegisterServiceRequest;
import com.dogfight.dogfight.api.service.user.response.UserResponse;
import com.dogfight.dogfight.api.service.user.response.UserTokenResponse;
import com.dogfight.dogfight.common.jwt.JwtProvider;
import com.dogfight.dogfight.domain.user.User;
import com.dogfight.dogfight.domain.user.UserRepository;
import io.jsonwebtoken.Jwt;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Date;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Log4j2
@Service
public class UserServiceImpl implements  UserService{
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;
    private final RedisTemplate<String, String> redisTemplate;
    @Override
    @Transactional
    public UserResponse register(UserRegisterServiceRequest userLoginServiceRequest, LocalDateTime registerDateTime) {
        User user = userLoginServiceRequest.toEntity();

        User savedUser = userRepository.save(user);


        return UserResponse.of(savedUser);
    }

    @Override
    @Transactional
    public UserTokenResponse login(UserRegisterServiceRequest userLoginServiceRequest, Date date) {
        try {

            String account = userLoginServiceRequest.getAccount();
            String password = userLoginServiceRequest.getPassword();

            User user = userRepository.findByAccountAndPassword(account, password)
                    .orElseThrow(() -> new BadCredentialsException("아이디 혹은 패스워드가 잘못되었습니다"));
//

            String accessToken = jwtProvider.createAccessToken(user.getAccount(), date);
            String refreshToken = jwtProvider.createRefreshToken(user.getAccount(),date);

            return UserTokenResponse.builder()
                    .requestToken(refreshToken)
                    .accessToken(accessToken)
                    .build();
        } catch(BadCredentialsException e){
            log.error("아이디 혹은 패스워드가 잘못되었습니다");
            throw new BadCredentialsException(e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e);
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

    @Override
    public UserTokenResponse refresh(UserServiceRefreshTokenRequest userServiceRefreshTokenRequest, Date date) {
        String refreshToken = userServiceRefreshTokenRequest.getRefreshToken();
        //refresh Token 검증
        jwtProvider.validateToken("refresh",refreshToken);

        String account = jwtProvider.getAccount(refreshToken);
        String redisRefreshToken = (String)redisTemplate.opsForValue().get(account);

        if(redisRefreshToken==null || !redisRefreshToken.equals(refreshToken)) {
            log.error("존재하지 않는 토큰입니다");
            throw new RuntimeException("존재하지 않는 토큰입니다");
        }

        return UserTokenResponse.builder()
                .accessToken(jwtProvider.createAccessToken(account,date))
                .requestToken(jwtProvider.createRefreshToken(account,date))
                .build();
    }
}
