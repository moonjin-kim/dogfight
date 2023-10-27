package com.dogfight.dogfight.api.service.user.response;

import com.dogfight.dogfight.domain.user.User;
import lombok.Builder;
import lombok.Getter;

@Getter
public class UserResponse {
    private Long id;
    private String userId;

    @Builder
    public UserResponse(Long id, String userId){
        this.id = id;
        this.userId = userId;
    }

    public static UserResponse of(User user){
        return UserResponse.builder()
                .id(user.getId())
                .userId(user.getAccount())
                .build();
    }
}
