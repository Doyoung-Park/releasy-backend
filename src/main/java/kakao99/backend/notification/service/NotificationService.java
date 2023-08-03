package kakao99.backend.notification.service;

import kakao99.backend.entity.Issue;
import kakao99.backend.entity.Member;
import kakao99.backend.entity.Notification;
import kakao99.backend.entity.Project;
import kakao99.backend.entity.types.NotificationType;
import kakao99.backend.issue.dto.DragNDropDTO;
import kakao99.backend.issue.repository.IssueRepository;
import kakao99.backend.notification.dto.NotificationDTO;
import kakao99.backend.notification.rabbitmq.dto.RequestMessageDTO;
import kakao99.backend.notification.repository.NotificationRepository;
import kakao99.backend.project.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class NotificationService {
    private final NotificationRepository notificationRepository;
    private final ProjectRepository projectRepository;
    private final IssueRepository issueRepository;

    @Transactional
    public void createNotification(RequestMessageDTO requestMessageDTO) {
        Long projectId = requestMessageDTO.getProjectId();
        Project project = projectRepository.findProjectById(projectId);

        System.out.println("requestMessageDTO.getType().getType() = " + requestMessageDTO.getType());

        if (requestMessageDTO.getType().equals(NotificationType.ISSUEDONE)) {
        Long issueId = requestMessageDTO.getSpecificTypeId();

            Issue issue = issueRepository.findById(issueId).get();
            String issueTitle = issue.getTitle();
            String notificationMessage = "'"+issueTitle + NotificationType.ISSUEDONE.getVerb();
            Notification newNotification = new Notification().builder()
                    .message(notificationMessage)
                    .type(requestMessageDTO.getType().getType())
                    .typeSpecificId(requestMessageDTO.getSpecificTypeId())
                    .project(project).build();
            notificationRepository.save(newNotification);

        } else if(requestMessageDTO.getType().equals(NotificationType.ISSUEDELETED)) {
            log.info("New Notification: 이슈 삭제");
            Long issueId = requestMessageDTO.getSpecificTypeId();
            System.out.println("issueId = " + issueId);

            Issue issue = issueRepository.findById(issueId).get();
            String issueTitle = issue.getTitle();

            String notificationMessage = "'"+issueTitle + NotificationType.ISSUEDELETED.getVerb();

            Notification newNotification = new Notification().builder()
                    .message(notificationMessage)
                    .type(requestMessageDTO.getType().getType())
                    .typeSpecificId(requestMessageDTO.getSpecificTypeId())
                    .project(project).build();
            notificationRepository.save(newNotification);
            System.out.println("된거야?");

        }else if(requestMessageDTO.getType().equals(NotificationType.ISSUECREATED)) {
            log.info("New Notification: 이슈 발행");
            Long issueId = requestMessageDTO.getSpecificTypeId();
            Issue issue = issueRepository.findById(issueId).get();
            String issueTitle = issue.getTitle();
            String myNickname = requestMessageDTO.getMyNickname();
            String notificationMessage = "'"+issueTitle+"' 이슈가 '"+ myNickname+NotificationType.ISSUECREATED.getVerb();
            Notification newNotification = new Notification().builder()
                    .message(notificationMessage)
                    .type(requestMessageDTO.getType().getType())
                    .typeSpecificId(requestMessageDTO.getSpecificTypeId())
                    .project(project).build();
            notificationRepository.save(newNotification);
        }else{
            System.out.println("Error");

        }
    }

    public List<NotificationDTO> getAllNotifications(Long projectId) {
        List<Notification> allNotificationOfProject = notificationRepository.findAllByProjectId(projectId);

        List<NotificationDTO> notificationDTOList = NotificationDTO.AllNotificationOfProject(allNotificationOfProject);

        return notificationDTOList;
    }
}
