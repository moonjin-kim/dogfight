package com.dogfight.dogfight.api.service.vote;

import com.dogfight.dogfight.api.service.vote.response.VoteResponse;

public interface VoteService {
    public VoteResponse vote(long id, int count);


}
