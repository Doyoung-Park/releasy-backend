package kakao99.backend.entity;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ReleaseNoteRepository extends JpaRepository<ReleaseNote, Long> {
    void findReleaseNoteById(Long releaseId);
}