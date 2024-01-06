package com.dogfight.dogfight.domain.comment;

import com.dogfight.dogfight.domain.BaseEntity;
import com.dogfight.dogfight.domain.board.Board;
import com.dogfight.dogfight.domain.user.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.boot.context.properties.bind.DefaultValue;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.FetchType.LAZY;

@Getter
@Entity
@Table
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 1000)
    private String content;

    @Column(nullable = false)
    private String password;

    private String nickname;

    private Long selectId;

    @ColumnDefault("FALSE")
    private Boolean isDeleted;


    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "parent_id")
    private Comment parent;

    @OneToMany(mappedBy = "parent", orphanRemoval = true)
    private List<Comment> children = new ArrayList<>();

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "board_id")
    private Board board;

    @Builder
    public Comment(Long id, String content, Boolean isDeleted, Long selectId, String nickname, String password, Comment parent, List<Comment> children, Board board) {
        this.id = id;
        this.content = content;
        this.isDeleted = isDeleted;
        this.nickname = nickname;
        this.password = password;
        this.parent = parent;
        this.selectId = selectId;
        this.children = children;
        this.board = board;
    }

    public void updateContent(String content) {
        this.content = content;
    }

    public void changeIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }
}
