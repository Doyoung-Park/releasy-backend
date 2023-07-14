package kakao99.backend.issue.test;

import kakao99.backend.entity.Issue;
import kakao99.backend.issue.repository.IssueRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TestService {

    private final IssueRepository issueRepository;

    public Issue getOneIssueByProjectId(Long issueId) {
        Optional<Issue> byId = issueRepository.findById(issueId);
        if (byId.isEmpty()) {
            throw new NoSuchElementException();
        }
        Issue myIssue = byId.get();
        return myIssue;
    }
}
