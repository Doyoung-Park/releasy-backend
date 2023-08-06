package kakao99.backend.release.repository;

import kakao99.backend.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface ReleaseParentChildRepository extends JpaRepository<ReleaseNoteParentChild, Long> {
    ReleaseNoteParentChild save(ReleaseNoteParentChild releaseNoteParentChild);
    List<ReleaseNoteParentChild> findByChildNoteAndIsActive(ReleaseNote childNote, Boolean isActive);


    ReleaseNoteParentChild findByChildNote(ReleaseNote childNote);

}
