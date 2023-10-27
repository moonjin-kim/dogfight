package com.dogfight.dogfight.api.controller.user.request;

import com.dogfight.dogfight.api.service.user.request.UserRegisterServiceRequest;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserRegisterRequest {
    private String account;
    private String password;
    private String email;
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
