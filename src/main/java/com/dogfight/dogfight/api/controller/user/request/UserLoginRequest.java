package com.dogfight.dogfight.api.controller.user.request;

import com.dogfight.dogfight.api.service.user.request.UserRegisterServiceRequest;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserLoginRequest {
    private String account;
    private String password;

    public UserLoginRequest(String account, String password) {
        this.account = account;
        this.password = password;
    }

    public UserRegisterServiceRequest toServiceRequest(){
        return UserRegisterServiceRequest.builder()
                .account(account)
                .password(password)
                .build();
    }
}
