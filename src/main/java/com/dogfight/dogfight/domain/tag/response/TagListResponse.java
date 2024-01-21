package com.dogfight.dogfight.domain.tag.response;

import lombok.Builder;
import lombok.Getter;

@Getter
public class TagListResponse {
    private Long id;
    private String name;
    private Long count;

    @Builder
    public TagListResponse(Long id, String name, Long count) {
        this.id = id;
        this.name = name;
        this.count = count;
    }
}
