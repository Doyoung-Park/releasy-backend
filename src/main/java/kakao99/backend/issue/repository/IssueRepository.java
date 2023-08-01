package kakao99.backend.issue.repository;

import jakarta.persistence.EntityManager;
import kakao99.backend.entity.Issue;
import kakao99.backend.entity.IssueParentChild;
import kakao99.backend.entity.Member;
import kakao99.backend.entity.Project;
import kakao99.backend.issue.dto.MemberInfoDTO;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface IssueRepository extends JpaRepository<Issue, Long>, IssueRepositoryCustom {
    Issue save(Issue issue);

    @Query("select m from Issue m where m.isActive = true")
    Optional<Issue> findIssueById(Long issueId);


    @Query("select m from Issue m join fetch m.project join fetch m.memberInCharge join fetch m.memberReport where m.project.id=:projectId and m.isActive = true")
    List<Issue> findAllByProjectId(@Param("projectId") Long projectId);


    Optional<Issue> findById(Long issueId);

    @Query("select m from Issue m join fetch m.project join fetch m.memberInCharge where m.project.id=:projectId and m.status = :status and m.isActive = true")
    List<Issue> findAllByStatus(@Param("projectId") Long projectId, @Param("status") String status);

    @Query("select m from Issue m join fetch m.project join fetch m.memberInCharge where m.project.id=:projectId and m.issueType = :issueType and m.isActive = true")
    List<Issue> findAllByType(@Param("projectId") Long projectId, @Param("issueType") String type);


    @Query("select m from Issue m join fetch m.project join fetch m.memberInCharge where m.project.id=:projectId and m.status = :status and m.issueType = :issueType and m.isActive = true")
    List<Issue> findAllByStatusAndType(@Param("projectId") Long projectId, @Param("status") String status,@Param("issueType") String type);


    @Query("select m from Issue m join fetch m.project join fetch m.memberInCharge where m.project.id=:projectId and m.status = :status and m.issueType = :issueType and m.memberInCharge.username = :username and m.isActive = true")
    List<Issue> findAllByStatusAndTypeAndUsername(@Param("projectId") Long projectId, @Param("status") String status,@Param("issueType") String type,@Param("username") String username);



    @Modifying
    @Query("UPDATE Issue p SET p.title = :title, p.description =:description WHERE p.id = :issueId")
    void updateIssue(@Param("title") String title, @Param("description") String description, @Param("issueId") Long issueId);

    @Modifying
    @Query("UPDATE Issue p SET p.title = :title WHERE p.id = :issueId")
    void updateIssueTitle(@Param("title") String title, @Param("issueId") Long issueId);

    @Modifying
    @Query("UPDATE Issue p SET p.status = :status WHERE p.id = :issueId")
    void updateIssueStatus(@Param("status") String status, @Param("issueId") Long issueId);


    @Modifying
    @Query("UPDATE Issue p SET p.issueType = :issueType WHERE p.id = :issueId")
    void updateIssueType(@Param("issueType") String issueType, @Param("issueId") Long issueId);

    @Modifying
    @Query("UPDATE Issue p SET p.description = :description WHERE p.id = :issueId")
    void updateIssueDescription(@Param("description") String description, @Param("issueId") Long issueId);

    @Query("select distinct i from Issue i left join i.releaseNote t where t.id =:releaseNoteId")
    List<Issue> getAllIssuesByReleaseNoteId(@Param("releaseNoteId") Long releaseNoteId);    // 문제 있음.


    @Query("select m from Issue m join fetch m.releaseNote join fetch m.memberInCharge join fetch m.memberReport where m.releaseNote.id =:releaseNoteId and m.isActive = true")
    List<Issue> findAllByReleaseNoteId(@Param("releaseNoteId") Long releaseNoteId);

//    @Query("select new kakao99.backend.issue.dto.IssueDTO(m.id, m.issueNum, m.title, m.issueType, m.description, m.description," +
//            "m.status, m.listPosition, m.file, m.createdAt, m.memberInCharge)from Issue m where m.releaseNote.id = :releaseNoteId")
//    List<Issue> findAllByReleaseNoteId(@Param("releaseNoteId") Long releaseNoteId);


//    @Query("select new kakao99.backend.issue.dto.MemberInfoDTO(m.username, m.nickname, m.email, m.position ) from Member m where m.id = :memberId")
//    MemberInfoDTO getMemberInfoDTO(@Param("memberId") Long memberId);


    // 프로젝트 내의 releaseNote에 포함되지 않은 이슈들 조회
@   Query("select m from Issue m join fetch m.memberInCharge join fetch m.memberReport where m.releaseNote.id = null and m.isActive = true and m.project.id =:projectId")
    List<Issue> findAllByNotReleaseNoteId(@Param("projectId") Long projectId);

    @Modifying
    @Query("UPDATE Issue m SET m.releaseNote.id = null where m.id =:issueId")
    int deleteIssueFromReleaseNote(@Param("issueId") Long issueId);

    @Modifying
    @Query("UPDATE Issue m SET m.releaseNote.id =:releaseNoteId  where m.id =:issueId")
    int insertIssueFromReleaseNote(@Param("releaseNoteId") Long releaseNoteId, @Param("issueId") Long issueId);


    @Query("SELECT MAX(i.id) FROM Issue i")
    Long findMaxId();
}
