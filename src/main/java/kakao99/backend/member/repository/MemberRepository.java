package kakao99.backend.member.repository;

import jakarta.persistence.EntityManager;
import kakao99.backend.entity.Member;
import kakao99.backend.entity.ReleaseNote;
import kakao99.backend.issue.dto.MemberInfoDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

//    EntityManager em;
    Member save(Member member);
    Optional<Member> findById(Long id);

    Optional<Member> findByEmail(String email);

    @Query("select new kakao99.backend.issue.dto.MemberInfoDTO(m.username, m.nickname, m.email, m.position ) from Member m where m.id = :memberId")
    MemberInfoDTO getMemberInfoDTO(@Param("memberId") Long memberId);




    /*

        private String name;
    private String nickname;
    private String email;
    private String position;
     */
}
