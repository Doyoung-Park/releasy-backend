package kakao99.backend.issue.repository;

import kakao99.backend.entity.Issue;
import kakao99.backend.issue.controller.UpdateIssueForm;
import kakao99.backend.issue.dto.DragNDropDTO;

import java.util.ArrayList;
import java.util.List;

public interface IssueRepositoryCustom {
    List<Issue> findAllWithFilter(Long projectId, String status, String type, String username);


    void updateIssue(UpdateIssueForm updateIssueForm, Long issueId);

    void updateIssueByDragNDrop(DragNDropDTO dragNDropDTO);

    void deleteIssue(Long issueId, Long memberId);

    List<Issue> getIssueListNotFinishedOf(Long projectId);

    void saveIssueImage(Long issueId, ArrayList<String> imgUrlList);

}
