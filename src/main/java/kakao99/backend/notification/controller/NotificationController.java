package kakao99.backend.notification.controller;

import kakao99.backend.common.ResponseMessage;
import kakao99.backend.entity.Member;
import kakao99.backend.entity.Notification;
import kakao99.backend.notification.dto.NotificationDTO;
import kakao99.backend.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

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






    @GetMapping(value ="/api/project/{projectId}/notification-stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter getNotificationStream(@PathVariable("projectId") Long projectId) throws IOException {

//        Member member = (Member) authentication.getPrincipal();



        SseEmitter emitter = new SseEmitter(); // 연결을 확립함.

        emitter.send("Connection  Success");

        // Service의 emitters 맵에 emitter 저장
        notificationService.addEmitter(projectId, emitter);


        emitter.onCompletion(() -> notificationService.removeEmitter(projectId ,emitter));
        emitter.onError((e) -> notificationService.removeEmitter(projectId, emitter));

        return emitter;
    }
}
