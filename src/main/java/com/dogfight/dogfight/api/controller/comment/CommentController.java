package com.dogfight.dogfight.api.controller.comment;


import com.dogfight.dogfight.api.ApiResponse;
import com.dogfight.dogfight.api.controller.comment.request.CommentRequest;
import com.dogfight.dogfight.api.service.comment.CommentService;
import com.dogfight.dogfight.api.service.comment.response.CommentResponse;
import com.dogfight.dogfight.common.jwt.JwtProvider;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RequestMapping("/api/v0/comment")
@RestController
@Slf4j
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;
    private final JwtProvider jwtProvider;

    @PostMapping("/new")
    public ApiResponse<String> create(CommentRequest request,
                                      HttpServletRequest httpServletRequest){
        LocalDateTime localDateTime = LocalDateTime.now();
        String account = jwtProvider.getAccountFromHeader(httpServletRequest);

        return ApiResponse.ok(
                commentService.create(request.toCommentServiceRequest())
        );
    }

    @PutMapping("/{id}")
    public ApiResponse<String> update(CommentRequest request,
                                      @RequestParam(value = "id") Long id,
                                      HttpServletRequest httpServletRequest) {
        LocalDateTime localDateTime = LocalDateTime.now();
        String account = jwtProvider.getAccountFromHeader(httpServletRequest);

        return ApiResponse.ok(
                commentService.update(request.toCommentServiceRequest(),id)
        );
    }

    @DeleteMapping("/{id}")
    public ApiResponse<String> delete(@RequestParam(value = "id") Long id,
                                      HttpServletRequest httpServletRequest) {
        LocalDateTime localDateTime = LocalDateTime.now();
        String account = jwtProvider.getAccountFromHeader(httpServletRequest);

        return ApiResponse.ok(
                commentService.delete(id,account)
        );
    }

    @GetMapping("/{id}")
    public ApiResponse<List<CommentResponse>> read(@RequestParam(value = "id") Long id) {
        LocalDateTime localDateTime = LocalDateTime.now();

        return ApiResponse.ok(
                commentService.findAllCommentByBoardId(id)
        );
    }


}
