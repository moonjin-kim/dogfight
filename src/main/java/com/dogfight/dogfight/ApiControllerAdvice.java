package com.dogfight.dogfight;

import com.dogfight.dogfight.api.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

@Slf4j
public class ApiControllerAdvice {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BindException.class)
    public ApiResponse<Object> bindException(BindException e){
        log.info("test");
        return ApiResponse.of(
                HttpStatus.BAD_REQUEST,
                "test1",
                null);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<String> badCredentialsException(BindException e){
        log.info("BadCredentialsException");
        return new ResponseEntity<>("test",HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(NullPointerException.class)
    public ApiResponse<Object> nullPointError(BindException e){
        log.info("패스워드 에러");
        return ApiResponse.of(
                HttpStatus.BAD_REQUEST,
                e.getMessage(),
                null);
    }


    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MissingServletRequestPartException.class)
    public ResponseEntity<String> exceptionError(MissingServletRequestPartException e) {
        log.info(e.getMessage());
        return new ResponseEntity<>("test",HttpStatus.BAD_REQUEST);
    }
}
