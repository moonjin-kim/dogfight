package com.dogfight.dogfight.api.controller.board;

import com.dogfight.dogfight.api.ApiResponse;
import com.dogfight.dogfight.api.controller.board.request.BoardCreateRequest;
import com.dogfight.dogfight.api.service.board.BoardServiceImpl;
import com.dogfight.dogfight.api.service.board.response.BoardResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RequestMapping("/api/v0/board")
@RestController
@RequiredArgsConstructor
public class BoardController {
    private final BoardServiceImpl boardService;
    @PostMapping("/new")
    public ApiResponse<BoardResponse> create(@RequestBody BoardCreateRequest request){
        LocalDateTime localDateTime = LocalDateTime.now();

        return ApiResponse.ok(
                boardService.create(request.toServiceRequest(),localDateTime)
        );
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
