package com.dogfight.dogfight.api.controller.user.request;

import com.dogfight.dogfight.api.service.user.request.UserServiceRefreshTokenRequest;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserRefreshTokenRequest {
    private String refreshToken;

    @Builder
    public UserRefreshTokenRequest(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public UserServiceRefreshTokenRequest toService(){
        return UserServiceRefreshTokenRequest.builder()
                .refreshToken(refreshToken)
                .build();
    }
}
