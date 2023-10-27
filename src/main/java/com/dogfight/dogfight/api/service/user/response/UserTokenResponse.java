package com.dogfight.dogfight.api.service.user.response;

import com.dogfight.dogfight.domain.user.User;
import lombok.Builder;
import lombok.Getter;

@Getter
public class UserTokenResponse {
    private String accessToken;
    private String requestToken;

    @Builder
    public UserTokenResponse(String accessToken, String requestToken){
        this.accessToken = accessToken;
        this.requestToken = requestToken;
    }

}
