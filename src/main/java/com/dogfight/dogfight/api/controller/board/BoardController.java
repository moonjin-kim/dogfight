package com.dogfight.dogfight.api.controller.board;

import com.dogfight.dogfight.api.ApiResponse;
import com.dogfight.dogfight.api.controller.board.request.BoardCreateRequest;
import com.dogfight.dogfight.api.service.board.BoardService;
import com.dogfight.dogfight.api.service.board.request.BoardCreateServiceRequest;
import com.dogfight.dogfight.api.service.board.response.BoardResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RequestMapping("/api/v0/board")
@RestController
@RequiredArgsConstructor
public class BoardController {
    private final BoardService boardService;
    @PostMapping("/new")
    public ApiResponse<BoardResponse> create(@RequestBody BoardCreateRequest request){
        LocalDateTime localDateTime = LocalDateTime.now();

        return ApiResponse.ok(
                boardService.create(request.toServiceRequest(),localDateTime)
        );
    }
}
