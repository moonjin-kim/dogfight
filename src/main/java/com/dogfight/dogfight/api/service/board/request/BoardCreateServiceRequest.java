package com.dogfight.dogfight.api.service.board.request;

import com.dogfight.dogfight.domain.board.Board;
import com.dogfight.dogfight.domain.tag.Tag;
import com.dogfight.dogfight.domain.user.User;
import com.dogfight.dogfight.domain.vote.Vote;
import lombok.Builder;
import lombok.Getter;
import org.hibernate.grammars.hql.HqlParser;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
public class BoardCreateServiceRequest {
    String title;
    String writer;
    String content;
    String tag;
    MultipartFile option1Image;
    MultipartFile option2Image;
    String option1;
    String option2;

    @Builder
    public BoardCreateServiceRequest(String title, String writer, String content, String tag, MultipartFile option1Image, MultipartFile option2Image, String option1, String option2) {
        this.title = title;
        this.writer = writer;
        this.content = content;
        this.tag = tag;
        this.option1Image = option1Image;
        this.option2Image = option2Image;
        this.option1 = option1;
        this.option2 = option2;
    }

    public Board toEntity(User user, Vote vote, Tag tag){
        return Board.builder()
                .title(title)
                .user(user)
                .views(0)
                .content(content)
                .vote(vote)
                .tag(tag)
                .build();
    }
}
