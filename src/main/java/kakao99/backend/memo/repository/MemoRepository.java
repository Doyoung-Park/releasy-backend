package kakao99.backend.memo.repository;


import kakao99.backend.entity.Memo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemoRepository extends JpaRepository<Memo, Long> {


    List<Memo> findAllByIssueId(Long issueId);
    Optional<Memo> findById(Long id);



}
