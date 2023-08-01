package kakao99.backend.notification.service;

import kakao99.backend.entity.Issue;
import kakao99.backend.entity.Member;
import kakao99.backend.entity.Notification;
import kakao99.backend.issue.dto.DragNDropDTO;
import kakao99.backend.notification.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final NotificationRepository notificationRepository;

    public Notification createNotification(DragNDropDTO dragNDropDTO, Member member, Issue issue) {

        Notification notification = Notification.createdByDragNDrop(dragNDropDTO, member, issue);
        notificationRepository.save(notification);
        return notification;
    }
}
