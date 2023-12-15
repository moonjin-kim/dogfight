package com.dogfight.dogfight.api.controller.api_error;

import com.dogfight.dogfight.api.controller.api_error.response.ErrorResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@RestController
public class ApiExceptionController {
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ResponseStatusException.class)
    public ErrorResult ResponseStatusHandler(ResponseStatusException e) {
        log.info("Error Catch");
        return new ErrorResult("403", e.getMessage());
    }
}
