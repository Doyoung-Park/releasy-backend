package kakao99.backend.notification.repository;

import com.querydsl.jpa.impl.JPAInsertClause;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import kakao99.backend.entity.QNotification;
import kakao99.backend.entity.QReleaseNote;
import kakao99.backend.entity.types.NotificationType;
import kakao99.backend.issue.repository.IssueParentChildRepository;
import kakao99.backend.notification.rabbitmq.dto.RequestMessageDTO;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
@RequiredArgsConstructor
@Slf4j
public class NotificationRepositoryImpl implements NotificationRepositoryCustom{
    private final EntityManager em;

    private final IssueParentChildRepository issueParentChildRepository;

    private final JPAQueryFactory query;
    private QNotification notification = QNotification.notification;


    public void createNotification(RequestMessageDTO requestMessageDTO){

        if (requestMessageDTO.getType().equals(NotificationType.ISSUEDONE)) {
            log.info("이슈 해결 요청");
            this.query.insert(notification)
                    .columns(notification.type, notification.typeSpecificId, notification.project.id, notification.message)
                    .values(requestMessageDTO.getType(), requestMessageDTO.getSpecificTypeId(), requestMessageDTO.getProjectId(), "이슈가 해결되었습니다.").execute();

        }


    }
}
