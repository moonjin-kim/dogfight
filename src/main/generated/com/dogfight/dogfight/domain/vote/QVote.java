package com.dogfight.dogfight.domain.vote;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QVote is a Querydsl query type for Vote
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QVote extends EntityPathBase<Vote> {

    private static final long serialVersionUID = 1088893873L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QVote vote = new QVote("vote");

    public final com.dogfight.dogfight.domain.board.QBoard board;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath option1 = createString("option1");

    public final NumberPath<Integer> option1Count = createNumber("option1Count", Integer.class);

    public final StringPath option1ImageUrl = createString("option1ImageUrl");

    public final StringPath option2 = createString("option2");

    public final NumberPath<Integer> option2Count = createNumber("option2Count", Integer.class);

    public final StringPath option2ImageUrl = createString("option2ImageUrl");

    public QVote(String variable) {
        this(Vote.class, forVariable(variable), INITS);
    }

    public QVote(Path<? extends Vote> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QVote(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QVote(PathMetadata metadata, PathInits inits) {
        this(Vote.class, metadata, inits);
    }

    public QVote(Class<? extends Vote> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.board = inits.isInitialized("board") ? new com.dogfight.dogfight.domain.board.QBoard(forProperty("board"), inits.get("board")) : null;
    }

}

