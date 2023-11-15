package com.dogfight.dogfight.api.controller.board.request;

import com.dogfight.dogfight.api.service.board.request.BoardCreateServiceRequest;
import lombok.Builder;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;
import java.util.List;

public class BoardCreateRequest implements Serializable {
    String title;
    String content;
    String tag;
    String option1;
    String option2;

    @Builder
    public BoardCreateRequest(String title,String content, String tag,String option1, String option2) {
        this.title = title;
        this.content = content;
        this.tag = tag;
        this.option1 = option1;
        this.option2 = option2;
    }

    public BoardCreateServiceRequest toServiceRequest(MultipartFile option1Image, MultipartFile option2Image, String writer) {
        return BoardCreateServiceRequest.builder()
                .title(title)
                .content(content)
                .writer(writer)
                .tag(tag)
                .option1(option1)
                .option1Image(option1Image)
                .option2(option2)
                .option2Image(option2Image)
                .build();
    }

}
