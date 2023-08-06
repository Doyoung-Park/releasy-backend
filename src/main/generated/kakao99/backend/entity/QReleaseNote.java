package kakao99.backend.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QReleaseNote is a Querydsl query type for ReleaseNote
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QReleaseNote extends EntityPathBase<ReleaseNote> {

    private static final long serialVersionUID = -647501261L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QReleaseNote releaseNote = new QReleaseNote("releaseNote");

    public final StringPath brief = createString("brief");

    public final ListPath<ReleaseNoteParentChild, QReleaseNoteParentChild> childNote = this.<ReleaseNoteParentChild, QReleaseNoteParentChild>createList("childNote", ReleaseNoteParentChild.class, QReleaseNoteParentChild.class, PathInits.DIRECT2);

    public final DateTimePath<java.util.Date> createdAt = createDateTime("createdAt", java.util.Date.class);

    public final DateTimePath<java.util.Date> deletedAt = createDateTime("deletedAt", java.util.Date.class);

    public final StringPath description = createString("description");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final BooleanPath isActive = createBoolean("isActive");

    public final QMember member;

    public final ListPath<ReleaseNoteParentChild, QReleaseNoteParentChild> parentNote = this.<ReleaseNoteParentChild, QReleaseNoteParentChild>createList("parentNote", ReleaseNoteParentChild.class, QReleaseNoteParentChild.class, PathInits.DIRECT2);

    public final NumberPath<Float> percent = createNumber("percent", Float.class);

    public final QProject project;

    public final DateTimePath<java.util.Date> releaseDate = createDateTime("releaseDate", java.util.Date.class);

    public final StringPath status = createString("status");

    public final DateTimePath<java.util.Date> updatedAt = createDateTime("updatedAt", java.util.Date.class);

    public final StringPath version = createString("version");

    public QReleaseNote(String variable) {
        this(ReleaseNote.class, forVariable(variable), INITS);
    }

    public QReleaseNote(Path<? extends ReleaseNote> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QReleaseNote(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QReleaseNote(PathMetadata metadata, PathInits inits) {
        this(ReleaseNote.class, metadata, inits);
    }

    public QReleaseNote(Class<? extends ReleaseNote> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.member = inits.isInitialized("member") ? new QMember(forProperty("member"), inits.get("member")) : null;
        this.project = inits.isInitialized("project") ? new QProject(forProperty("project"), inits.get("project")) : null;
    }

}

