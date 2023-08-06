package kakao99.backend.notification.controller;

import kakao99.backend.common.ResponseMessage;
import kakao99.backend.entity.Notification;
import kakao99.backend.notification.dto.NotificationDTO;
import kakao99.backend.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping("api/project/{projectId}/notification")
    public ResponseEntity<?> getNotification(@PathVariable("projectId") Long projectId) {

        log.info("로그 조회");

        List<NotificationDTO> allNotificationOfProject = notificationService.getAllNotifications(projectId);

        ResponseMessage message = new ResponseMessage(200, projectId+"번 프로젝트의 Notification 조회 성공", allNotificationOfProject);
        return new ResponseEntity(message, HttpStatus.OK);
    }
}
