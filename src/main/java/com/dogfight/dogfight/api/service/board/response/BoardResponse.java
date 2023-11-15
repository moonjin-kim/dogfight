package com.dogfight.dogfight.api.service.board.response;

import com.dogfight.dogfight.domain.board.Board;
import com.dogfight.dogfight.domain.vote.Vote;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class BoardResponse {
    Long id;
    String title;
    String writer;
    String content;
    LocalDateTime registeredDateTime;
    int views;
    String tag;
    Vote vote;

    @Builder
    public BoardResponse(Long id,String title, String writer,LocalDateTime registeredDateTime, String content, int views, String tag, Vote vote) {
        this.id = id;
        this.title = title;
        this.writer = writer;
        this.content = content;
        this.registeredDateTime = registeredDateTime;
        this.views = views;
        this.tag = tag;
        this.vote = vote;
    }

    public static BoardResponse of(Board board){
        return BoardResponse.builder()
                .id(board.getId())
                .title(board.getTitle())
                .writer(board.getUser().getNickname())
                .content(board.getContent())
                .registeredDateTime(board.getCreatedDateTime())
                .views(board.getViews())
                .tag(board.getTag().getName())
                .vote(board.getVote())
                .build();
    }


}