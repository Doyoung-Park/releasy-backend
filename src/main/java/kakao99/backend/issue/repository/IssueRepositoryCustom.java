package kakao99.backend.issue.repository;

import kakao99.backend.entity.Issue;
import kakao99.backend.issue.controller.UpdateIssueForm;

import java.util.List;

public interface IssueRepositoryCustom {
    public List<Issue> findAllWithFilter(Long projectId, String status, String type, String username);

    public void updateIssue(UpdateIssueForm updateIssueForm, Long issueId);
}
