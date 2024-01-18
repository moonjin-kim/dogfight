package com.dogfight.dogfight.api.service.board;

import com.dogfight.dogfight.api.service.board.request.BoardCreateServiceRequest;
import com.dogfight.dogfight.api.service.board.request.BoardUpdateServiceRequest;
import com.dogfight.dogfight.api.service.board.response.BoardResponse;
import com.dogfight.dogfight.domain.tag.Tag;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.List;

public interface BoardService {
    public BoardResponse create(BoardCreateServiceRequest request, LocalDateTime localDateTime);
    public Page<BoardResponse> search(int pageNo, int pageSize, String criteria, String sort, String name, String title);
    public BoardResponse read(Long id);
    public BoardResponse next();
    public BoardResponse update(BoardUpdateServiceRequest request);
    public List<Tag> getTag();
    public Boolean delete(Long id);
}
