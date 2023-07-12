package kakao99.backend.project.repository;


import kakao99.backend.entity.MemberProject;
import kakao99.backend.entity.Project;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import kakao99.backend.entity.*;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public class MemberProjectRepository{

    private final EntityManager em;
    private final JPAQueryFactory query;

    private QMember member = QMember.member;

    private QMemberProject memberProject = QMemberProject.memberProject;

    private QProject project = QProject.project;
    private QGroup group = QGroup.group;

    public MemberProjectRepository(EntityManager em) {
        this.em = em;
        this.query = new JPAQueryFactory(em);
    }

    public MemberProject save(MemberProject memberProject) {
        em.persist(memberProject);
        return memberProject;
    }

    public Optional<MemberProject> findAllByProjectIdAndMemberId(Long projectId, Long memberId) {
        return Optional.ofNullable(query
                .select(memberProject)
                .from(memberProject)
                .where(memberProject.project.id.eq(projectId).and(memberProject.member.id.eq(memberId)))
                .fetchOne());
    }


    public List<Member> findMemberByProjectId(Long projectId) {
        return query
                .select(memberProject.member)
                .from(memberProject)
                .where(memberProject.project.id.eq(projectId))
                .fetch();
    }

    public List<Project> findProjectByMemberId(Long memberId) {
        return query
                .select(project)
                .from(memberProject)
                .where(memberProject.member.id.eq(memberId))
                .fetch();
    }

}
