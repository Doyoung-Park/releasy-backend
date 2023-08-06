package kakao99.backend.notification.service;

import kakao99.backend.common.exception.CustomException;
import kakao99.backend.entity.*;
import kakao99.backend.entity.types.NotificationType;
import kakao99.backend.issue.dto.DragNDropDTO;
import kakao99.backend.issue.repository.IssueRepository;
import kakao99.backend.notification.dto.NotificationDTO;
import kakao99.backend.notification.rabbitmq.dto.RequestMessageDTO;
import kakao99.backend.notification.repository.NotificationRepository;
import kakao99.backend.project.repository.ProjectRepository;
import kakao99.backend.release.repository.ReleaseRepository;
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
    private final ReleaseRepository releaseRepository;
    private final NotificationRepository notificationRepository;
    private final ProjectRepository projectRepository;
    private final IssueRepository issueRepository;

    @Transactional
    public void createNotification(RequestMessageDTO requestMessageDTO) {
        Long projectId = requestMessageDTO.getProjectId();
        Project project = projectRepository.findProjectById(projectId);

        if (requestMessageDTO.getType().equals(NotificationType.ISSUEDONE)) {
            log.info("New Notification: 이슈 해결");
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
        } else if (requestMessageDTO.getType().equals(NotificationType.RELEASENOTECHANGED)) {
            log.info("New Notification: 릴리즈노트 수정");
            Long releaseNoteId = requestMessageDTO.getSpecificTypeId();
            ReleaseNote releaseNote = releaseRepository.findById(releaseNoteId).get();

            String notificationMessage = "ver."+releaseNote.getVersion() + NotificationType.RELEASENOTECHANGED.getVerb();
            Notification newNotification = new Notification().builder()
                    .message(notificationMessage)
                    .type(requestMessageDTO.getType().getType())
                    .typeSpecificId(releaseNoteId)
                    .project(project).build();
            notificationRepository.save(newNotification);
        } else if (requestMessageDTO.getType().equals(NotificationType.RELEASENOTECREATED)) {
            log.info("New Notification: 릴리즈노트 생성");
            Long releaseNoteId = requestMessageDTO.getSpecificTypeId();
            ReleaseNote releaseNote = releaseRepository.findById(releaseNoteId).get();

            String notificationMessage = "ver."+releaseNote.getVersion() + NotificationType.RELEASENOTECREATED.getVerb();
            Notification newNotification = new Notification().builder()
                    .message(notificationMessage)
                    .type(requestMessageDTO.getType().getType())
                    .typeSpecificId(releaseNoteId)
                    .project(project).build();
            notificationRepository.save(newNotification);
        }else if (requestMessageDTO.getType().equals(NotificationType.RELEASENOTEDELETED)) {
            log.info("New Notification: 릴리즈노트 삭제");
            Long releaseNoteId = requestMessageDTO.getSpecificTypeId();
            ReleaseNote releaseNote = releaseRepository.findById(releaseNoteId).get();

            String notificationMessage = "ver."+releaseNote.getVersion() + NotificationType.RELEASENOTEDELETED.getVerb();
            Notification newNotification = new Notification().builder()
                    .message(notificationMessage)
                    .type(requestMessageDTO.getType().getType())
                    .typeSpecificId(releaseNoteId)
                    .project(project).build();
            notificationRepository.save(newNotification);
        }else if (requestMessageDTO.getType().equals(NotificationType.NEWMEMBER)) {
            log.info("New Notification: 새로운 멤버 참여");

            String notificationMessage = "프로젝트에 새로운 멤버 '"+requestMessageDTO.getMyNickname() + NotificationType.NEWMEMBER.getVerb();
            Notification newNotification = new Notification().builder()
                    .message(notificationMessage)
                    .type(requestMessageDTO.getType().getType())
                    .typeSpecificId(requestMessageDTO.getSpecificTypeId())
                    .project(project).build();
            notificationRepository.save(newNotification);
        }else if (requestMessageDTO.getType().equals(NotificationType.OUTMEMBER)) {
            log.info("New Notification: 멤버 탈퇴");

            String notificationMessage = "프로젝트에서 '"+requestMessageDTO.getMyNickname() + NotificationType.OUTMEMBER.getVerb();
            Notification newNotification = new Notification().builder()
                    .message(notificationMessage)
                    .type(requestMessageDTO.getType().getType())
                    .typeSpecificId(requestMessageDTO.getSpecificTypeId())
                    .project(project).build();
            notificationRepository.save(newNotification);
        }else {
            throw new CustomException(500, "Notification 생성 Error");
        }
    }

    public List<NotificationDTO> getAllNotifications(Long projectId) {
        List<Notification> allNotificationOfProject = notificationRepository.findAllByProjectId(projectId);

        List<NotificationDTO> notificationDTOList = NotificationDTO.AllNotificationOfProject(allNotificationOfProject);

        return notificationDTOList;
    }
}
