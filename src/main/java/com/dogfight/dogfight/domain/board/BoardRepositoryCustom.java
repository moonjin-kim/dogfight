package com.dogfight.dogfight.domain.board;

import com.dogfight.dogfight.api.service.board.response.BoardResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BoardRepositoryCustom {
    Page<BoardResponse> boardList(Pageable pageable);
    Board increaseViewsAndReturnBoard(long id);
    BoardResponse selectRandBoard();
}
