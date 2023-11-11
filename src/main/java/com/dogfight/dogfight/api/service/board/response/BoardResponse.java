package com.dogfight.dogfight.api.service.board.response;

import com.dogfight.dogfight.domain.board.Board;
import com.dogfight.dogfight.domain.vote.Vote;
import lombok.Builder;

public class BoardResponse {
    Long id;
    String writer;
    String content;
    int views;
    String tag;
    Vote vote;

    @Builder
    public BoardResponse(Long id, String writer, String content, int views, String tag, Vote vote) {
        this.id = id;
        this.writer = writer;
        this.content = content;
        this.views = views;
        this.tag = tag;
        this.vote = vote;
    }

    public static BoardResponse of(Board board){
        return BoardResponse.builder()
                .id(board.getId())
                .writer(board.getUser().getNickname())
                .content(board.getContent())
                .views(board.getViews())
                .tag(board.getTag().getName())
                .vote(board.getVote())
                .build();
    }


}
