package com.dogfight.dogfight.api.controller.board.request;

import com.dogfight.dogfight.api.service.board.request.BoardCreateServiceRequest;
import lombok.Builder;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public class BoardCreateRequest {
    String title;
    String writer;
    String content;
    String tag;
    MultipartFile option1Image;
    MultipartFile option2Image;
    String option1;
    String option2;

    @Builder
    public BoardCreateRequest(String title, String writer, String content, String tag, MultipartFile option1Image, MultipartFile option2Image, String option1, String option2) {
        this.title = title;
        this.writer = writer;
        this.content = content;
        this.tag = tag;
        this.option1Image = option1Image;
        this.option2Image = option2Image;
        this.option1 = option1;
        this.option2 = option2;
    }

    public BoardCreateServiceRequest toServiceRequest() {
        return BoardCreateServiceRequest.builder()
                .title(title)
                .writer(writer)
                .content(content)
                .tag(tag)
                .option1(option1)
                .option1Image(option1Image)
                .option2(option2)
                .option2Image(option2Image)
                .build();
    }

}
