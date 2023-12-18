package com.dogfight.dogfight.api.service.user.response;

import lombok.Builder;
import lombok.Getter;

@Getter
public class UserResponse {
    long id;
    String nickname;

    @Builder
    public UserResponse(long id, String nickname) {
        this.id = id;
        this.nickname = nickname;
    }
}
