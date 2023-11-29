package com.dogfight.dogfight.api.controller.board;

import com.dogfight.dogfight.api.ApiResponse;
import com.dogfight.dogfight.api.controller.board.request.BoardCreateRequest;
import com.dogfight.dogfight.api.service.board.BoardService;
import com.dogfight.dogfight.api.service.board.response.BoardResponse;
import com.dogfight.dogfight.common.jwt.JwtProvider;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@RequestMapping("/api/v0/board")
@RestController
@Slf4j
@RequiredArgsConstructor
public class BoardController {
    private final BoardService boardService;
    private final JwtProvider jwtProvider;
    @PostMapping("/new")
    public ApiResponse<BoardResponse> create(BoardCreateRequest request,
                                             HttpServletRequest httpServletRequest,
                                             @RequestPart("option1Image") MultipartFile option1Image,
                                             @RequestPart("option2Image") MultipartFile option2Image){
        LocalDateTime localDateTime = LocalDateTime.now();
        String accessToken = jwtProvider.resolveToken(httpServletRequest);
        String account = jwtProvider.getAccount(accessToken.replace("Bearer ", ""));

        return ApiResponse.ok(
                boardService.create(request.toServiceRequest(option1Image, option2Image,account),localDateTime)
        );
    }

    @GetMapping
    public ApiResponse<Page<BoardResponse>> getBoardsPage(
            @RequestParam(required = false, defaultValue = "0", value = "page") int pageNo,
            @RequestParam(required = false, defaultValue = "0", value = "pageSize") int pageSize,
            @RequestParam(required = false, defaultValue = "views", value = "page") String criteria,
            @RequestParam(required = false, defaultValue = "DESC", value = "sort") String sort) {
        return ApiResponse.ok(boardService.getBoardsPage(pageNo, pageSize, criteria, sort));
    }

    @GetMapping("/{id}")
    public ApiResponse<BoardResponse> read(@PathVariable("id") Long id){
        return ApiResponse.ok(
                boardService.read(id)
        );
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Boolean> delete(@PathVariable("id") Long id){
        return ApiResponse.ok(
                boardService.delete(id)
        );
    }
}
