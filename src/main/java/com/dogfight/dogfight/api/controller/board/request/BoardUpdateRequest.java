package com.dogfight.dogfight.api.controller.board.request;

import com.dogfight.dogfight.api.service.board.request.BoardCreateServiceRequest;
import com.dogfight.dogfight.api.service.board.request.BoardUpdateServiceRequest;
import lombok.Builder;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;

public class BoardUpdateRequest implements Serializable {
    Long id;
    String title;
    String content;
    Long voteId;
    String tag;
    String option1;
    String option2;

    @Builder
    public BoardUpdateRequest(Long id,String title, String content,Long voteId, String tag, String option1, String option2) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.voteId = voteId;
        this.tag = tag;
        this.option1 = option1;
        this.option2 = option2;
    }

    public BoardUpdateServiceRequest toServiceRequest(MultipartFile option1Image, MultipartFile option2Image, String writer) {
        return BoardUpdateServiceRequest.builder()
                .id(id)
                .title(title)
                .content(content)
                .writer(writer)
                .tag(tag)
                .voteId(voteId)
                .option1(option1)
                .option1Image(option1Image)
                .option2(option2)
                .option2Image(option2Image)
                .build();
    }

}
