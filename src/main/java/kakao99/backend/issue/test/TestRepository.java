package kakao99.backend.issue.test;

import kakao99.backend.entity.Issue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface TestRepository extends JpaRepository<Issue, Long> {
    Issue save(Issue issue);

//    Issue findByProjectIdAndIssueId(Long projectId, Long issueId);

//    @Query("select m from Issue m join fetch m.project")
//    List<Issue> findAllByProjectId(Long projectId);

    @Query("select m from Issue m join fetch m.project join fetch m.memberInCharge join fetch m.memberReport where m.project.id=:projectId and m.isActive = true")
    Optional<Issue> findAllByProjectId(@Param("projectId") Long projectId);


    @Modifying
    @Transactional
    @Query("UPDATE Issue p SET p.title = :title, p.description =:description WHERE p.id = :issueId")
    void updateIssue(@Param("title") String title, @Param("description") String description, @Param("issueId") Long issueId);

    @Modifying
    @Transactional
    @Query("UPDATE Issue p SET p.title = :title WHERE p.id = :issueId")
    void updateIssueTitle(@Param("title") String title, @Param("issueId") Long issueId);

    @Modifying
    @Transactional
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
@Query("select m from Issue m join fetch m.memberInCharge join fetch m.memberReport where m.releaseNote.id = null and m.isActive = true and m.project.id =:projectId")
List<Issue> findAllByNotReleaseNoteId(@Param("projectId") Long projectId);


    @Modifying
    @Transactional
    @Query("UPDATE Issue m SET m.releaseNote.id = null where m.id =:issueId")
    int deleteTest(@Param("issueId") Long issueId);

    @Modifying
    @Transactional
    @Query("UPDATE Issue m SET m.releaseNote.id =:releaseNoteId  where m.id =:issueId")
    int updateTest(@Param("releaseNoteId") Long releaseNoteId, @Param("issueId") Long issueId);


}
