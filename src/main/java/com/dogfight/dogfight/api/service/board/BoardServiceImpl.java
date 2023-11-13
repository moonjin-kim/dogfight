package com.dogfight.dogfight.api.service.board;

import com.dogfight.dogfight.api.service.board.request.BoardCreateServiceRequest;
import com.dogfight.dogfight.api.service.board.response.BoardResponse;
import com.dogfight.dogfight.common.filehandler.FileHandler;
import com.dogfight.dogfight.domain.board.Board;
import com.dogfight.dogfight.domain.board.BoardRepository;
import com.dogfight.dogfight.domain.heart.HeartRepository;
import com.dogfight.dogfight.domain.tag.Tag;
import com.dogfight.dogfight.domain.user.User;
import com.dogfight.dogfight.domain.user.UserRepository;
import com.dogfight.dogfight.domain.vote.Vote;
import com.dogfight.dogfight.domain.vote.VoteRepository;
import com.dogfight.dogfight.domain.tag.TagRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Transactional
@RequiredArgsConstructor
@Service
@Slf4j
public class BoardServiceImpl {

    private final BoardRepository boardRepository;
    private final VoteRepository voteRepository;
    private final UserRepository userRepository;
    private final TagRepository tagRepository;
    private final HeartRepository heartRepository;
    private final FileHandler fileHandler;

    public BoardResponse create(BoardCreateServiceRequest request, LocalDateTime localDateTime){

        User user = userRepository.findByNickname(request.getWriter());

        Vote vote = createVote(request.getOption1(),
                request.getOption2(),
                request.getOption1Image(),
                request.getOption2Image());


        Tag tag = findTagBy(request.getTag());

        Board board = request.toEntity(user,vote,tag);
        Board saveBoard = boardRepository.save(board);

        return BoardResponse.of(saveBoard);
    }

    public BoardResponse read(Long id){
        Board board = boardRepository.findById(id).orElseThrow();

        return BoardResponse.of(board);
    }

    public boolean delete(Long id){
        boardRepository.deleteById(id);

        return true;
    }

    private Tag findTagBy(String tag){
        return tagRepository.findByName(tag).orElseGet(()->createTag(tag));
    }

    private Tag createTag(String tag){
        Tag tagEntity = Tag.builder()
                .name(tag)
                .build();

        return tagRepository.save(tagEntity);
    }

    private Vote createVote(String option1, String option2, MultipartFile option1Image, MultipartFile option2Image){
        String option1ImageUrl = saveImage(option1Image);
        String option2ImageUrl = saveImage(option2Image);

        Vote vote = Vote.builder()
                .option1(option1)
                .option2(option2)
                .option1ImageUrl(option1ImageUrl)
                .option2ImageUrl(option2ImageUrl)
                .option1Count(0)
                .option2Count(0)
                .build();

        return voteRepository.save(vote);
    }

    private String saveImage(MultipartFile image){
        LocalDateTime localDateTime = LocalDateTime.now();
        try{
            return fileHandler.saveImage(image,localDateTime);
        } catch (Exception e){
            log.error(e.getMessage());
        }

        return null;
    }
}
