package com.dogfight.dogfight.api.controller.user.request;

import com.dogfight.dogfight.api.service.user.request.UserRegisterServiceRequest;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserRegisterRequest {

    @NotEmpty
    private String account;

    @NotEmpty
    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*\\W).{8,20}$", message = "비밀번호 형식을 맞춰야합니다")
    private String password;

    @NotEmpty
    @Pattern(regexp = "^[a-zA-Z0-9+-\\_.]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$",
            message = "이메일 형식을 맞춰야합니다")
    private String email;

    @NotEmpty
    private String nickname;

    @Builder
    public UserRegisterRequest(String account, String password, String email, String nickname) {
        this.account = account;
        this.password = password;
        this.email = email;
        this.nickname = nickname;
    }


    public UserRegisterServiceRequest toServiceRequest(){
        return UserRegisterServiceRequest.builder()
                .account(account)
                .password(password)
                .email(email)
                .nickname(nickname)
                .build();
    }

}
