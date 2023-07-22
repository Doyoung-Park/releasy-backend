package kakao99.backend.issue.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.jpa.impl.JPAUpdateClause;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import kakao99.backend.common.exception.CustomException;
import kakao99.backend.common.exception.ErrorCode;
import kakao99.backend.entity.*;
import kakao99.backend.issue.controller.UpdateIssueForm;
import kakao99.backend.issue.dto.DragNDropDTO;
import kakao99.backend.issue.dto.IssueDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class IssueRepositoryImpl implements IssueRepositoryCustom {

    private final EntityManager em;

    private final JPAQueryFactory query;

    private QIssue issue = QIssue.issue;
    private QProject project = QProject.project;

    private QReleaseNote releaseNote = QReleaseNote.releaseNote;

    private QMember member = QMember.member;

    public List<Issue> findAllWithFilter(Long projectId, String status, String type, String username) {
        JPAQuery<Issue> query = this.query.selectFrom(issue)
                .where(issue.project.id.eq(projectId).and(issue.isActive.eq(true)));

        if (status != null) {
            query.where(issue.status.eq(status));
        }

        if (type != null) {
            query.where(issue.issueType.eq(type));
        }

        if (username != null) {
            query.where(issue.memberInCharge.username.eq(username));
        }

        return query.fetch();
    }

    @Transactional
    public void updateIssue(UpdateIssueForm updateIssueForm, Long issueId) {

        JPAUpdateClause query = this.query.update(issue)
                .where(issue.id.eq(issueId).and(issue.isActive.eq(true)));

        if(updateIssueForm.getTitle() != null){
            query.set(issue.title, updateIssueForm.getTitle());
        }

        if (updateIssueForm.getDescription() != null) {
            query.set(issue.description, updateIssueForm.getDescription());
        }

        if (updateIssueForm.getIssueType() != null) {
            query.set(issue.issueType, updateIssueForm.getIssueType());
        }

        if (updateIssueForm.getStatus() != null) {
            query.set(issue.status, updateIssueForm.getStatus());
        }
        if (updateIssueForm.getTitle()==null && updateIssueForm.getDescription()==null &&
                updateIssueForm.getIssueType()==null && updateIssueForm.getStatus()==null) {
            throw new CustomException(ErrorCode.INVALID_INPUT_VALUE);
        }
        query.execute();
    }

    @Transactional
    public void updateIssueByDragNDrop(DragNDropDTO dragNDropDTO) {
        JPAUpdateClause query = this.query.update(issue)
                .where(issue.id.eq(dragNDropDTO.getIssueId()).and(issue.isActive.eq(true)));

        if (dragNDropDTO.getDestinationStatus() != null) {
            query.set(issue.status, dragNDropDTO.getDestinationStatus());
        }

        if (dragNDropDTO.getSourceStatus() != null) {
            query.set(issue.status, dragNDropDTO.getDestinationStatus());
        }

        if(dragNDropDTO.getDestinationStatus() == null && dragNDropDTO.getSourceStatus() == null){
            throw new CustomException(ErrorCode.INVALID_INPUT_VALUE);
        }
        query.execute();
    }

}
