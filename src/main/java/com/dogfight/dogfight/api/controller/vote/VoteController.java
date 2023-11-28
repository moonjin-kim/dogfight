package com.dogfight.dogfight.api.controller.vote;

import com.dogfight.dogfight.api.ApiResponse;
import com.dogfight.dogfight.api.service.vote.VoteService;
import com.dogfight.dogfight.api.service.vote.response.VoteResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v0/vote")
@RestController
public class VoteController {

    private final VoteService voteService;
    @GetMapping("/{id}/{voteNum}")
    public ApiResponse<VoteResponse> vote(@PathVariable("id") Long id, @PathVariable("voteNum") int voteNum){
        return ApiResponse.ok(
            voteService.vote(id, voteNum)
        );
    }
}
