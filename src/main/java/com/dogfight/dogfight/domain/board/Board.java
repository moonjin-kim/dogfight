package com.dogfight.dogfight.domain.board;

import com.dogfight.dogfight.domain.BaseEntity;
import com.dogfight.dogfight.domain.tag.Tag;
import com.dogfight.dogfight.domain.user.User;
import com.dogfight.dogfight.domain.vote.Vote;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Board extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String content;

    private int views;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "tag_id")
    private Tag tag;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="vote_id")
    private Vote vote;

    @Builder
    public Board(String title, String content, int views, User user, Tag tag, Vote vote) {
        this.title = title;
        this.content = content;
        this.views = views;
        this.user = user;
        this.tag = tag;
        this.vote = vote;
    }
}
