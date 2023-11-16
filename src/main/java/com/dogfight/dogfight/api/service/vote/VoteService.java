package com.dogfight.dogfight.api.service.vote;

import com.dogfight.dogfight.api.service.vote.response.VoteResponse;
import com.dogfight.dogfight.domain.vote.Vote;
import com.dogfight.dogfight.domain.vote.VoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class VoteService {
    private final VoteRepository voteRepository;

    public VoteResponse vote(long id, int count){
        if(count == 1){
            voteRepository.incrementFirstOptionCount(id);
        } else if(count == 2){
            voteRepository.incrementSecondOptionCount(id);
        }

        Vote result = voteRepository.findById(id).orElseThrow();

        return VoteResponse.of(result);
    }

}
