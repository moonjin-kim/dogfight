package com.dogfight.dogfight.api.service.user.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserServiceRequest {

    @NotEmpty(message = "ID는 필수입니다.")
    private String userId;
    @NotEmpty(message = "비밀번호는 필수입니다.")
    private String password;

    @Builder
    public UserServiceRequest(String userId, String password) {
        this.userId = userId;
        this.password = password;
    }
}
