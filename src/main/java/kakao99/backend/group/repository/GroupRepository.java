package kakao99.backend.group.repository;

import kakao99.backend.entity.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GroupRepository extends JpaRepository<Group, Long> {
    Group save(Group group);

    Optional<Group> findByCode(String code);

    Optional<Group> findById(Long Id);
}
