package kakao99.backend.issue.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import kakao99.backend.entity.*;
import kakao99.backend.issue.dto.IssueDTO;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class IssueRepositoryImpl {

    private final EntityManager em;

    private final JPAQueryFactory query;

    public IssueRepositoryImpl(EntityManager em) {
        this.em = em;
        this.query = new JPAQueryFactory(em);
    }

    private QIssue issue = QIssue.issue;
    private QProject project = QProject.project;

    private QReleaseNote releaseNote = QReleaseNote.releaseNote;

    private QMember member = QMember.member;

//    @Query("select m from Issue m join fetch m.project join fetch m.memberInCharge where m.project.id=:projectId and m.isActive = true")
//    List<Issue> findAllByProjectId(@Param("projectId") Long projectId);

//    public List<?> findAllByProjectIdImpl(Long projectId) {
//        return query.select(Projections.bean(IssueDTO.class, issue.id, issue.issueNum, issue.title,
//                        issue.issueType, issue.description, issue.status, issue.listPosition,
//                        issue.file, issue.createdAt, issue.memberInCharge))
//                .from(issue)
//                .innerJoin(issue.project, project)
//                .where(issue.project.id.eq(projectId).and(issue.isActive.eq(true)))
//                .stream().toList();
//
////
////        IssueDTO.class, issue.title, issue.issueNum))
////                .from(issue)
////                .innerJoin(issue.project, project)
////                .where(issue.project.id.eq(projectId).and(issue.isActive.eq(true)))
////                .stream().forEach();
//    }

//    public List<?> findAllByProjectIdImpl(Long projectId) {
//
//        return query.select(new QIssueDTO(issue.id, issue.issueNum, issue.title,
//                        issue.issueType, issue.description, issue.status, issue.listPosition,
//                        issue.file, issue.createdAt, ))
//                .from(issue)
//                .innerJoin(issue.project, project)
//                .innerJoin(issue.memberInCharge, member)
//                .where(issue.project.id.eq(projectId).and(issue.isActive.eq(true)))
//                .stream().toList();

//
//        IssueDTO.class, issue.title, issue.issueNum))
//                .from(issue)
//                .innerJoin(issue.project, project)
//                .where(issue.project.id.eq(projectId).and(issue.isActive.eq(true)))
//                .stream().forEach();
//    }
}
