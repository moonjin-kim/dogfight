package com.dogfight.dogfight.api.service.user.request;

import com.dogfight.dogfight.domain.user.Role;
import com.dogfight.dogfight.domain.user.User;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserRegisterServiceRequest {

    @NotEmpty(message = "ID는 필수입니다.")
    private String account;
    @NotEmpty(message = "비밀번호는 필수입니다.")
    private String password;

    private String nickname;

    private String email;

    @Builder
    public UserRegisterServiceRequest(String account, String password, String nickname, String email) {
        this.account = account;
        this.password = password;
        this.nickname = nickname;
        this.email = email;
    }

    public User toEntity(){
        return User.builder()
                .account(account)
                .password(password)
                .nickname(nickname)
                .email(email)
                .role(Role.USER)
                .build();
    }
}
