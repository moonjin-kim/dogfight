package com.dogfight.dogfight.domain.tag;

import com.dogfight.dogfight.domain.tag.response.TagListResponse;

import java.util.List;

public interface TagRepositoryCustom {
    List<TagListResponse> getTagListAndCount();
}
