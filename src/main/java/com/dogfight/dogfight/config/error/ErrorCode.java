package com.dogfight.dogfight.config.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ErrorCode {

    //400 BAD_REQUEST 잘못된 요청
    INVALID_PARAMETER(400, "파라미터 값을 확인해주세요."),

    //401 Unauthorized 권한 없음
    UNKNOWN_USER(401, "존재하지 않는 유저입니다."),
    BAD_CREDENTIALS_EXCEPTION(401, "잘못된 패스워드입니다. 다시 시도해주세요"),
    INTERNAL_AUTHENTICATION_SERVICE_EXCEPTION(401,"아이디가 틀렸습니다. 다시 시도해주세요."),

    //404 Error Not Found
    BOARD_NOT_FOUND(404, "게시글을 찾을 수 없습니다."),
    IMAGE_NOT_FOUND(404, "이미지를 찾을 수 없습니다"),
    VOTE_NOT_FOUND(404, "투표가 존재하지 않습니다"),
    COMMENT_NOT_FOUND(404, "댓글이 존재하지 않습니다."),

    //415
    IMAGE_TYPE_ERROR(415, "이미지 형식이 잘못되었습니다.");

    private final int status;
    private final String message;
}

