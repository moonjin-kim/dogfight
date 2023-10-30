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
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Log4j2
@Service
public class UserServiceImpl implements  UserService{
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;
    private final RedisTemplate<String, String> redisTemplate;
    private final PasswordEncoder bCryptPasswordEncoder;
    @Override
    @Transactional
    public UserResponse register(UserRegisterServiceRequest userLoginServiceRequest, LocalDateTime registerDateTime) {
        User user = userLoginServiceRequest.toEntity();

        checkAccountDuplicate(user.getAccount());
        user.hashPassword(bCryptPasswordEncoder);

        User savedUser = userRepository.save(user);

        return UserResponse.of(savedUser);
    }

    @Override
    @Transactional
    public UserTokenResponse login(UserRegisterServiceRequest userLoginServiceRequest, Date date) {
        try {

            String account = userLoginServiceRequest.getAccount();
            String password = userLoginServiceRequest.getPassword();

            User user = userRepository.findByAccount(account)
                    .orElseThrow(() -> new BadCredentialsException("아이디 혹은 패스워드가 잘못되었습니다"));

            if(!user.checkPassword(password,bCryptPasswordEncoder)) {
                throw new BadCredentialsException("아이디 혹은 패스워드가 잘못되었습니다");
            }

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
        String refresh = request.getRefreshToken();

        if(!jwtProvider.validateToken("refresh", refresh)){
            throw new BadCredentialsException("로그아웃에 실패하였습니다");
        }

        String account = jwtProvider.getAccount(refresh);

        redisTemplate.delete(account);

        return true;
    }

    @Override
    public boolean withdraw(UserRegisterServiceRequest userLoginServiceRequest, LocalDateTime registerDateTime) {

        return false;
    }

    @Override
    @Transactional
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

    public boolean ConfirmationPassword(String pwd){
        //비밀번호 재대로 작성했는지 테스트
        if(pwd.length() == 0){
            return false;
        }

        // 비밀번호 포맷 확인(영문, 특수문자, 숫자 포함 8자 이상)
        Pattern passPattern1 = Pattern.compile("^(?=.*[a-zA-Z])(?=.*\\d)(?=.*\\W).{8,20}$");
        Matcher passMatcher1 = passPattern1.matcher(pwd);

        if(!passMatcher1.find()){
            return false;
        }
        return true;
    }
    // 중복 검사 메서드 (이미 존재하는 상품인지 확인하고, 있다면 에러를 throw한다)
    private void checkAccountDuplicate(String name) {
        if (isExistedAccount(name)) {
            throw new DuplicateKeyException("이미 존재하는 아이디입니다."); //UnCheckedException으로 구현함
        }
    }

    // 이미 존재하는지 확인하는 메서드
    private boolean isExistedAccount(String name) {
        return userRepository.findByAccount(name).isPresent();
    }
}
