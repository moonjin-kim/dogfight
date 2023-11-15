package com.dogfight.dogfight.api.service.user.response;

import com.dogfight.dogfight.domain.user.User;
import lombok.Builder;
import lombok.Getter;

@Getter
public class UserServiceResponse {
    private Long id;
    private String userId;

    @Builder
    public UserServiceResponse(Long id, String userId){
        this.id = id;
        this.userId = userId;
    }

    public static UserServiceResponse of(User user){
        return UserServiceResponse.builder()
                .id(user.getId())
                .userId(user.getAccount())
                .build();
    }
}
