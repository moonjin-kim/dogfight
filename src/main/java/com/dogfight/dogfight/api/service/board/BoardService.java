package com.dogfight.dogfight.api.service.board;

import com.dogfight.dogfight.api.service.board.request.BoardCreateServiceRequest;
import com.dogfight.dogfight.api.service.board.request.BoardUpdateServiceRequest;
import com.dogfight.dogfight.api.service.board.response.BoardResponse;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;

public interface BoardService {
    public BoardResponse create(BoardCreateServiceRequest request, LocalDateTime localDateTime);
    public Page<BoardResponse> getBoardsPage(int pageNo, int pageSize, String criteria, String sort);
    public BoardResponse read(Long id);
    public BoardResponse update(BoardUpdateServiceRequest request);
    public Boolean delete(Long id);
}
