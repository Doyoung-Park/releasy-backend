package kakao99.backend.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QIssueParentChild is a Querydsl query type for IssueParentChild
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QIssueParentChild extends EntityPathBase<IssueParentChild> {

    private static final long serialVersionUID = 1113979551L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QIssueParentChild issueParentChild = new QIssueParentChild("issueParentChild");

    public final QIssue childIssue;

    public final DateTimePath<java.util.Date> createdAt = createDateTime("createdAt", java.util.Date.class);

    public final DateTimePath<java.util.Date> deletedAt = createDateTime("deletedAt", java.util.Date.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final BooleanPath isActive = createBoolean("isActive");

    public final QIssue parentIssue;

    public final DateTimePath<java.util.Date> updatedAt = createDateTime("updatedAt", java.util.Date.class);

    public QIssueParentChild(String variable) {
        this(IssueParentChild.class, forVariable(variable), INITS);
    }

    public QIssueParentChild(Path<? extends IssueParentChild> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QIssueParentChild(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QIssueParentChild(PathMetadata metadata, PathInits inits) {
        this(IssueParentChild.class, metadata, inits);
    }

    public QIssueParentChild(Class<? extends IssueParentChild> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.childIssue = inits.isInitialized("childIssue") ? new QIssue(forProperty("childIssue"), inits.get("childIssue")) : null;
        this.parentIssue = inits.isInitialized("parentIssue") ? new QIssue(forProperty("parentIssue"), inits.get("parentIssue")) : null;
    }

}

