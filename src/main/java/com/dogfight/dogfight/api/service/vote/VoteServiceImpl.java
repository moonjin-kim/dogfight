package com.dogfight.dogfight.api.service.vote;

import com.dogfight.dogfight.api.service.vote.response.VoteResponse;
import com.dogfight.dogfight.config.error.CustomException;
import com.dogfight.dogfight.config.error.ErrorCode;
import com.dogfight.dogfight.domain.vote.Vote;
import com.dogfight.dogfight.domain.vote.VoteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class VoteServiceImpl implements VoteService{
    private final VoteRepository voteRepository;

    public VoteResponse vote(long id, int count){
        log.info("id = {}, count = {}",id,count);
        if(count == 1){
            voteRepository.incrementFirstOptionCount(id);
        } else if(count == 2){
            voteRepository.incrementSecondOptionCount(id);
        } else {
            throw new CustomException("잘못된 투표 번호 입니다.", ErrorCode.INVALID_PARAMETER);
        }

        Vote result = voteRepository.findById(id).orElseThrow();

        return VoteResponse.of(result);
    }

}
