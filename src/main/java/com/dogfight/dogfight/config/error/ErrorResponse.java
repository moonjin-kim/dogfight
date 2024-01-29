package com.dogfight.dogfight.config.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
public class ErrorResponse {
    private String message;
    private int status;

    public ErrorResponse(ErrorCode code) {
        this.message = code.getMessage();
        this.status = code.getStatus();
    }

    public ErrorResponse(String message, int status) {
        this.message = message;
        this.status =status;
    }

    public static ErrorResponse of(ErrorCode code){
        return new ErrorResponse(code);
    }


}
