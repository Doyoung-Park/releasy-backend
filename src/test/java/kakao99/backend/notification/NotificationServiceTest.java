package kakao99.backend.notification;

import kakao99.backend.config.TestConfig;
import kakao99.backend.entity.Notification;
import kakao99.backend.entity.Project;
import kakao99.backend.entity.types.NotificationType;
import kakao99.backend.issue.repository.IssueRepository;
import kakao99.backend.issue.service.IssueService;
import kakao99.backend.notification.rabbitmq.dto.RequestMessageDTO;
import kakao99.backend.notification.repository.NotificationRepository;
import kakao99.backend.notification.service.NotificationService;
import kakao99.backend.project.repository.ProjectRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@ExtendWith(MockitoExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
//@ContextConfiguration(classes = BackendApplication.class)
@Import(TestConfig.class)
public class NotificationServiceTest {

    @InjectMocks
    NotificationService notificationService;

    //    @Mock
    @Autowired
    NotificationRepository notificationRepository;

    @Autowired
    ProjectRepository projectRepository;

    @DisplayName("이슈 상태 변경 테스트")
    @Test
    void changedIssueStatus() {
        RequestMessageDTO requestMessageDTO = new RequestMessageDTO().builder()
                .type(NotificationType.ISSUECHANGED)
                .specificTypeId(1L)
                .projectId(1L).build();

        notificationService.createNotification(requestMessageDTO);
    }


    @DisplayName("이슈 완료 - 상태 변경 테스트")
    @Test
    void changedIssueStatusDone() {
        RequestMessageDTO requestMessageDTO = new RequestMessageDTO().builder()
                .type(NotificationType.ISSUEDONE)
                .specificTypeId(1L)
                .projectId(2L).build();

        notificationService.createNotification(requestMessageDTO);
    }

    @DisplayName("Notification 저장 테스트")
    @Test
    void createNotification() {
        Project project = new Project();

        Notification newNotification = new Notification().builder()
                .message("이슈가 해결되었습니다.")
                .type("done")
                .typeSpecificId(1L)
                .project(project).build();
        notificationRepository.save(newNotification);
    }
}
