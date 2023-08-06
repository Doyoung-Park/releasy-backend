package kakao99.backend.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QIssue is a Querydsl query type for Issue
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QIssue extends EntityPathBase<Issue> {

    private static final long serialVersionUID = 745168371L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QIssue issue = new QIssue("issue");

    public final ListPath<IssueParentChild, QIssueParentChild> childIssues = this.<IssueParentChild, QIssueParentChild>createList("childIssues", IssueParentChild.class, QIssueParentChild.class, PathInits.DIRECT2);

    public final DateTimePath<java.util.Date> createdAt = createDateTime("createdAt", java.util.Date.class);

    public final DateTimePath<java.util.Date> deletedAt = createDateTime("deletedAt", java.util.Date.class);

    public final StringPath description = createString("description");

    public final StringPath file = createString("file");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Integer> importance = createNumber("importance", Integer.class);

    public final BooleanPath isActive = createBoolean("isActive");

    public final NumberPath<Integer> issueNum = createNumber("issueNum", Integer.class);

    public final StringPath issueType = createString("issueType");

    public final NumberPath<Integer> listPosition = createNumber("listPosition", Integer.class);

    public final QMember memberInCharge;

    public final QMember memberReport;

    public final ListPath<IssueParentChild, QIssueParentChild> parentIssues = this.<IssueParentChild, QIssueParentChild>createList("parentIssues", IssueParentChild.class, QIssueParentChild.class, PathInits.DIRECT2);

    public final QProject project;

    public final QReleaseNote releaseNote;

    public final StringPath status = createString("status");

    public final StringPath title = createString("title");

    public final DateTimePath<java.util.Date> updatedAt = createDateTime("updatedAt", java.util.Date.class);

    public QIssue(String variable) {
        this(Issue.class, forVariable(variable), INITS);
    }

    public QIssue(Path<? extends Issue> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QIssue(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QIssue(PathMetadata metadata, PathInits inits) {
        this(Issue.class, metadata, inits);
    }

    public QIssue(Class<? extends Issue> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.memberInCharge = inits.isInitialized("memberInCharge") ? new QMember(forProperty("memberInCharge"), inits.get("memberInCharge")) : null;
        this.memberReport = inits.isInitialized("memberReport") ? new QMember(forProperty("memberReport"), inits.get("memberReport")) : null;
        this.project = inits.isInitialized("project") ? new QProject(forProperty("project"), inits.get("project")) : null;
        this.releaseNote = inits.isInitialized("releaseNote") ? new QReleaseNote(forProperty("releaseNote"), inits.get("releaseNote")) : null;
    }

}

