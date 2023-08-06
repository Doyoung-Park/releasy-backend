package kakao99.backend.issue.repository;

import kakao99.backend.entity.Issue;
import kakao99.backend.entity.IssueParentChild;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IssueParentChildRepository extends JpaRepository <IssueParentChild, Long> {
    IssueParentChild save(IssueParentChild issue);

    List<IssueParentChild> findByparentIssue(Issue parentIssue);
    List<IssueParentChild> findBychildIssue(Issue childIssue);
    List<IssueParentChild> findByChildIssueAndIsActive(Issue childIssue, Boolean isActive);



    boolean existsByChildIssue(Issue issue);
}

