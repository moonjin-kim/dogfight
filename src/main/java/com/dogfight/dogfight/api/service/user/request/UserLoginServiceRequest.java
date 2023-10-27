package com.dogfight.dogfight.api.service.user.request;

import com.dogfight.dogfight.domain.user.User;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;

public class UserLoginServiceRequest {
    @NotEmpty(message = "ID는 필수입니다.")
    private String account;
    @NotEmpty(message = "비밀번호는 필수입니다.")
    private String password;

    @Builder
    public UserLoginServiceRequest(String account, String password) {
        this.account = account;
        this.password = password;
    }

}
