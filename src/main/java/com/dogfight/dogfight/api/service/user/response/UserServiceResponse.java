package com.dogfight.dogfight.api.service.user.response;

import com.dogfight.dogfight.domain.user.User;
import lombok.Builder;
import lombok.Getter;

@Getter
public class UserServiceResponse {
    Long id;
    String userId;
    String nickname;

    @Builder
    public UserServiceResponse(Long id, String userId,String nickname){
        this.id = id;
        this.userId = userId;
        this.nickname = nickname;
    }

    public static UserServiceResponse of(User user){
        return UserServiceResponse.builder()
                .id(user.getId())
                .userId(user.getAccount())
                .nickname(user.getNickname())
                .build();
    }
}
