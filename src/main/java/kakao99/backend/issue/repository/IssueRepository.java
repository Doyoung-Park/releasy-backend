package kakao99.backend.issue.repository;

import kakao99.backend.entity.Issue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IssueRepository extends JpaRepository<Issue, Long> {
    Issue save(Issue issue);
}
