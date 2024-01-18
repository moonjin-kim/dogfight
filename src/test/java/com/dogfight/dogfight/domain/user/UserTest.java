package com.dogfight.dogfight.domain.user;

import com.dogfight.dogfight.IntegrationTestSupport;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;

class UserTest  extends IntegrationTestSupport {
    private final PasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

    @DisplayName("비밀번호를 암호화 한다")
    @Test
    void encodePassword(){
        //given
        String plainPassword = "!test2342";
        User user = User.builder()
                .account("test123")
                .password(plainPassword)
                .build();

        //when
        user.hashPassword(bCryptPasswordEncoder);


        //then
        Assertions.assertThat(user.getPassword()).isNotEqualTo(plainPassword);
    }

    @DisplayName("plainPassword가 같으면 비밀번호를 비교할 수 있다")
    @Test
    void checkEncodePassword(){
        String plainPassword = "!test2342";
        User user = User.builder()
                .account("test123")
                .password(plainPassword)
                .build();

        //when
        user.hashPassword(bCryptPasswordEncoder);


        //then
        Assertions.assertThat(user.checkPassword(plainPassword,bCryptPasswordEncoder)).isTrue();
    }
}