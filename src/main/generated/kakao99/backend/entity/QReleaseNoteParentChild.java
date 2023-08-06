package kakao99.backend.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QReleaseNoteParentChild is a Querydsl query type for ReleaseNoteParentChild
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QReleaseNoteParentChild extends EntityPathBase<ReleaseNoteParentChild> {

    private static final long serialVersionUID = -1742584225L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QReleaseNoteParentChild releaseNoteParentChild = new QReleaseNoteParentChild("releaseNoteParentChild");

    public final QReleaseNote childNote;

    public final DateTimePath<java.util.Date> createdAt = createDateTime("createdAt", java.util.Date.class);

    public final DateTimePath<java.util.Date> deletedAt = createDateTime("deletedAt", java.util.Date.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final BooleanPath isActive = createBoolean("isActive");

    public final QReleaseNote parentNote;

    public final DateTimePath<java.util.Date> updatedAt = createDateTime("updatedAt", java.util.Date.class);

    public QReleaseNoteParentChild(String variable) {
        this(ReleaseNoteParentChild.class, forVariable(variable), INITS);
    }

    public QReleaseNoteParentChild(Path<? extends ReleaseNoteParentChild> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QReleaseNoteParentChild(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QReleaseNoteParentChild(PathMetadata metadata, PathInits inits) {
        this(ReleaseNoteParentChild.class, metadata, inits);
    }

    public QReleaseNoteParentChild(Class<? extends ReleaseNoteParentChild> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.childNote = inits.isInitialized("childNote") ? new QReleaseNote(forProperty("childNote"), inits.get("childNote")) : null;
        this.parentNote = inits.isInitialized("parentNote") ? new QReleaseNote(forProperty("parentNote"), inits.get("parentNote")) : null;
    }

}

