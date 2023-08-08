package kakao99.backend.member.repository;

import kakao99.backend.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

//    EntityManager em;
    Member save(Member member);
    Optional<Member> findById(Long id);

    Optional<Member> findByEmail(String email);

    Optional<Member> findByUsername(String name);

    /*
    @Query("select new kakao99.backend.issue.dto.MemberInfoDTO(m.username, m.nickname, m.email, m.position ) from Member m where m.id = :memberId")
    MemberInfoDTO getMemberInfoDTO(@Param("memberId") Long memberId);
    */
    List<Member> findByGroupIdAndIsActiveTrue(Long id);

    /*
    private String name;
    private String nickname;
    private String email;
    private String position;
     */

    @Modifying
    @Transactional
    @Query(value = "UPDATE Members SET exp = exp + :importance WHERE member_id = :memberId", nativeQuery = true)
    void updateExp(@Param("memberId") Long memberId, @Param("importance") int importance);
}
