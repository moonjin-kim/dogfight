package com.dogfight.dogfight.domain.board;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QBoard is a Querydsl query type for Board
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QBoard extends EntityPathBase<Board> {

    private static final long serialVersionUID = 691559713L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QBoard board = new QBoard("board");

    public final com.dogfight.dogfight.domain.QBaseEntity _super = new com.dogfight.dogfight.domain.QBaseEntity(this);

    public final StringPath content = createString("content");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdDateTime = _super.createdDateTime;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedDateTime = _super.modifiedDateTime;

    public final com.dogfight.dogfight.domain.tag.QTag tag;

    public final StringPath title = createString("title");

    public final com.dogfight.dogfight.domain.user.QUser user;

    public final NumberPath<Integer> views = createNumber("views", Integer.class);

    public final com.dogfight.dogfight.domain.vote.QVote vote;

    public QBoard(String variable) {
        this(Board.class, forVariable(variable), INITS);
    }

    public QBoard(Path<? extends Board> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QBoard(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QBoard(PathMetadata metadata, PathInits inits) {
        this(Board.class, metadata, inits);
    }

    public QBoard(Class<? extends Board> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.tag = inits.isInitialized("tag") ? new com.dogfight.dogfight.domain.tag.QTag(forProperty("tag")) : null;
        this.user = inits.isInitialized("user") ? new com.dogfight.dogfight.domain.user.QUser(forProperty("user")) : null;
        this.vote = inits.isInitialized("vote") ? new com.dogfight.dogfight.domain.vote.QVote(forProperty("vote"), inits.get("vote")) : null;
    }

}

