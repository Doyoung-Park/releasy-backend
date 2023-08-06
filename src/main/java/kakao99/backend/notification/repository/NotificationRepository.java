package kakao99.backend.notification.repository;

import kakao99.backend.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long>, NotificationRepositoryCustom {

    List<Notification> findAllByProjectId(Long projectId);


}
