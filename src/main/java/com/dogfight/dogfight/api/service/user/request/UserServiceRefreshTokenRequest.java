package com.dogfight.dogfight.api.service.user.request;

import lombok.Builder;
import lombok.Getter;

@Getter
public class UserServiceRefreshTokenRequest {
    private String refreshToken;

    @Builder
    public UserServiceRefreshTokenRequest(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
