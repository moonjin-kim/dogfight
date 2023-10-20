package com.dogfight.dogfight.api.controller.user.request;

import com.dogfight.dogfight.api.service.user.request.UserServiceRequest;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserRequest {
    private String userId;
    private String password;

    @Builder
    public UserRequest(String userId, String password) {
        this.userId = userId;
        this.password = password;
    }

    public UserServiceRequest toServiceRequest(){
        return UserServiceRequest.builder()
                .userId(userId)
                .password(password)
                .build();
    }

}
