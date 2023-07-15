package kakao99.backend.issue.test;

import kakao99.backend.entity.Issue;
import kakao99.backend.issue.exception.CustomException;
import kakao99.backend.issue.repository.IssueRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TestService {

    private final TestRepository testRepository;

    public Issue getOneIssueByIssueId(Long issueId){
//                    throw new NoSuchElementException();
        Optional<Issue> byId = testRepository.findById(issueId);
        if (byId.isEmpty()) {


            throw new CustomException(999, "커스텀에러!", "TestService.getOneIssueByIssueId");
//            throw new NoSuchElementException();
        }

        Issue myIssue = byId.get();
        return myIssue;
    }

//    public List<Issue> getAllIssueByProjectId(Long projectId) {
//        Optional<Issue> allByProjectId = testRepository.findAllByProjectId(projectId);
//        allByProjectId.orElse()
//
//    }

}
