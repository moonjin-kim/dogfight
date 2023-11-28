package com.dogfight.dogfight.domain.vote;

import com.dogfight.dogfight.domain.board.Board;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Vote {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String option1;
    private String option2;
    private String option1ImageUrl;
    private String option2ImageUrl;
    private int option1Count;
    private int option2Count;

    @OneToOne(mappedBy = "vote",fetch = FetchType.LAZY)
    private Board board;

    @Builder
    public Vote(Long id, String option1, String option2, String option1ImageUrl, String option2ImageUrl, int option1Count, int option2Count) {
        this.id = id;
        this.option1 = option1;
        this.option2 = option2;
        this.option1ImageUrl = option1ImageUrl;
        this.option2ImageUrl = option2ImageUrl;
        this.option1Count = option1Count;
        this.option2Count = option2Count;
    }
}
