package com.dogfight.dogfight.domain.board;

import com.dogfight.dogfight.api.service.board.response.BoardResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BoardRepositoryCustom {
    List<Board> search(Pageable pageable, String name , String tag);
    Board increaseViewsAndReturnBoard(long id);
    BoardResponse selectRandBoard();
}
