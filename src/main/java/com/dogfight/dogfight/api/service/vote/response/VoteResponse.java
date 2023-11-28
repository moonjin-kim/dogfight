package com.dogfight.dogfight.api.service.vote.response;

import com.dogfight.dogfight.domain.vote.Vote;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
public class VoteResponse {
    private Long id;
    private String option1;
    private String option2;
    private String option1Image;
    private String option2Image;
    private int option1Count;
    private int option2Count;

    @Builder
    public VoteResponse(Long id, String option1, String option2, String option1Image, String option2Image, int option1Count, int option2Count) {
        this.id = id;
        this.option1 = option1;
        this.option2 = option2;
        this.option1Image = option1Image;
        this.option2Image = option2Image;
        this.option1Count = option1Count;
        this.option2Count = option2Count;
    }

    public static VoteResponse of(Vote vote){
        log.info("vote ID = {}", vote.getId());
        return VoteResponse.builder()
                .id(vote.getId())
                .option1(vote.getOption1())
                .option2(vote.getOption2())
                .option1Image(vote.getOption1ImageUrl())
                .option2Image(vote.getOption2ImageUrl())
                .option1Count(vote.getOption1Count())
                .option2Count(vote.getOption2Count())
                .build();
    }

}
