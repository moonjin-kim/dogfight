package com.dogfight.dogfight.api.service.vote;

import com.dogfight.dogfight.domain.vote.VoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VoteService {
    private final VoteRepository questionRepository;



}
