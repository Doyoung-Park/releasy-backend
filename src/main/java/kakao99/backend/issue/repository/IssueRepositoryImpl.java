package kakao99.backend.issue.repository;

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
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.*;


import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

import static com.querydsl.core.group.GroupBy.groupBy;


@Repository
@RequiredArgsConstructor
public class IssueRepositoryImpl implements IssueRepositoryCustom {

    private final EntityManager em;

    private final IssueParentChildRepository issueParentChildRepository;

    private final JPAQueryFactory query;

    private QIssue issue = QIssue.issue;
    private QProject project = QProject.project;

    private QReleaseNote releaseNote = QReleaseNote.releaseNote;

    private QIssueParentChild issueParentChild = QIssueParentChild.issueParentChild;

    private QMember member = QMember.member;

//    private QIssueGrassDTO issueGrassDTO = QIssueGrassDTO();



// 제외할 id(excludeId)를 받아오면 childIssue와 issueParentChild와 같은 excludeId가 있는지 확인.
// 확인 후 List로 저장
    public List<Long> findExcludeId(Long projectId, Long excludeId) {
        JPAQuery<IssueParentChild> query = this.query.selectFrom(issueParentChild)
                .where(issueParentChild.isActive.eq(true)
                        .and(issueParentChild.parentIssue.project.id.eq(projectId)
                                .and(issueParentChild.childIssue.project.id.eq(projectId)
                                        .and(issueParentChild.parentIssue.id.eq(excludeId))
                                        )));

        List<IssueParentChild> issues = query.fetch();

        List<Long> results = new ArrayList<>();

        for (IssueParentChild issue : issues) {

    //        if (issue.getParentIssue().getId().equals(excludeId)){
    //            List<IssueParentChild> otherChildIssues = issueParentChildRepositiory.findByparentIssue(issue.getChildIssue());
    //            for (IssueParentChild childIssue : otherChildIssues) {
    //
    //                if(!results.contains(issue.getChildIssue().getId())){
    //                    results.add(childIssue.getChildIssue().getId());
    //                }
    //            }
    //        }
    //
    //        if (issue.getChildIssue().getId().equals(excludeId)) {
    //            List<IssueParentChild> otherChildIssues = issueParentChildRepositiory.findByparentIssue(issue.getParentIssue());
    //            for (IssueParentChild childIssue : otherChildIssues) {
    //
    //                if(!results.contains(issue.getChildIssue().getId())){
    //                results.add(childIssue.getChildIssue().getId());
    //                }
    //            }

    //            List<IssueParentChild> otherParentIssues = issueParentChildRepositiory.findBychildIssue(issue.getParentIssue());
    //            for (IssueParentChild parentIssue : otherParentIssues) {
    //
    //                if(!results.contains(issue.getParentIssue().getId())){
    //                    results.add(parentIssue.getParentIssue().getId());
    //                }
    //            }
    //        }

        if(!results.contains(issue.getChildIssue().getId())){
            results.add(issue.getChildIssue().getId());
        }

        if (!results.contains(issue.getParentIssue().getId())) {
            results.add(issue.getParentIssue().getId());
        }


    }
    if (results.isEmpty()) {
        results.add(excludeId);
    }
    return results;

}

    public List<Issue> findWithoutExcludeId(Long projectId,  List<Long> excludeIdList) {
        JPAQuery<Issue> query = this.query.selectFrom(issue)
                .where(issue.project.id.eq(projectId)
                        .and(issue.isActive.eq(true))
                        .and(issue.id.notIn(excludeIdList)));

        return query.fetch();
    }


    public List<Issue> findAllWithFilter(Long projectId, String status, String type, String username) {
        // Define initial query
        JPAQuery<Issue> query = this.query.selectFrom(issue)
                .leftJoin(issueParentChild)
                .on(issueParentChild.parentIssue.id.eq(issue.id))
                .where(issue.project.id.eq(projectId)
                        .and(issue.isActive.eq(true)));

        // Apply additional filters if they are not null
        if (status != null) {
            query.where(issue.status.eq(status));
        }

        if (type != null) {
            query.where(issue.issueType.eq(type));
        }

        if (username != null) {
            query.where(issue.memberInCharge.username.eq(username));
        }

        // Fetch the child issues for the issues that meet the criteria
        List<Issue> childIssues = query.fetch();

        return childIssues;
    }

    @Transactional
    public void updateIssue(UpdateIssueForm updateIssueForm, Long issueId) {

        JPAUpdateClause query = this.query.update(issue)
                .where(issue.id.eq(issueId).and(issue.isActive.eq(true)))
                .set(issue.updatedAt, new Date());

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
                .where(issue.id.eq(dragNDropDTO.getIssueId()).and(issue.isActive.eq(true)))
                .set(issue.updatedAt, new Date());;

        if (dragNDropDTO.getDestinationStatus() != null) {
            query.set(issue.status, dragNDropDTO.getDestinationStatus());
        }

        if (dragNDropDTO.getSourceStatus() != null) {
            query.set(issue.status, dragNDropDTO.getDestinationStatus());
        }

        if(dragNDropDTO.getDestinationStatus() == null && dragNDropDTO.getSourceStatus() == null){
            throw new CustomException(ErrorCode.INVALID_INPUT_VALUE);
        }

        if(dragNDropDTO.getListPosition() != null ){
            query.set(issue.listPosition, dragNDropDTO.getListPosition());
        }

        query.execute();
    }

    @Transactional
    public void deleteIssue(Long issueId, Long memberId) {
        long execute1 = this.query.update(issue)
                .set(issue.isActive, false)
                .set(issue.deletedAt, new Date())
                .where(issue.id.eq(issueId))
                .execute();

        long execute = this.query.update(issueParentChild)
                .set(issueParentChild.isActive, false)
                .set(issueParentChild.deletedAt, new Date())
                .where(issueParentChild.parentIssue.id.eq(issueId).or(issueParentChild.childIssue.id.eq(issueId)))
                .execute();
    }

    @Transactional
    public void deleteChild(Long issueId, Long childissueId) {

        long execute = this.query.update(issueParentChild)
                .set(issueParentChild.isActive, false)
                .set(issueParentChild.deletedAt, new Date())
                .where(issueParentChild.parentIssue.id.eq(issueId).and(issueParentChild.childIssue.id.eq(childissueId)))
                .execute();
    }

    public List<Issue> getIssueListNotFinishedOf(Long projectId) {
        JPAQuery<Issue> query = this.query.selectFrom(issue)
                .where(issue.project.id.eq(projectId).and(issue.isActive.eq(true))
                        .and(issue.status.ne("done")));

        List<Issue> issueList = query.fetch();

        if (issueList == null || issueList.isEmpty()) {
            throw new NoSuchElementException("릴리즈 노트에 포함되지 않은 이슈가 없습니다.");
        }
        return issueList;
    }

    public void saveIssueImage(Long issueId, ArrayList<String> imgUrlList) {
        if (imgUrlList.size() == 1) {
            this.query.update(issue)
                    .set(issue.imgUrl_1, imgUrlList.get(0))
                    .set(issue.imgUrl_2, "")
                    .set(issue.imgUrl_3, "")
                    .where(issue.id.eq(issueId)).execute();
        } else if (imgUrlList.size() == 2) {
            this.query.update(issue)
                    .set(issue.imgUrl_1, imgUrlList.get(0))
                    .set(issue.imgUrl_2, imgUrlList.get(1))
                    .set(issue.imgUrl_3, "")
                    .where(issue.id.eq(issueId)).execute();
        } else if (imgUrlList.size() == 3) {
            this.query.update(issue)
            .set(issue.imgUrl_1, imgUrlList.get(0))
                    .set(issue.imgUrl_2, imgUrlList.get(1))
                    .set(issue.imgUrl_3, imgUrlList.get(2))
                    .where(issue.id.eq(issueId)).execute();
        }

    }

}
