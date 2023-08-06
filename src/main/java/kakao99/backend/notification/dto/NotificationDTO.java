package kakao99.backend.notification.dto;

import kakao99.backend.entity.Notification;
import kakao99.backend.issue.dto.GPTQuestionDTO;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@Builder
public class NotificationDTO {
    private String message;
    private String type;

    private Long typeSpecificId; // 타입에 해당하는 특정id
    private Date createdAt; // 생성일

    public static List<NotificationDTO> AllNotificationOfProject(List<Notification> notificationList) {

        List<NotificationDTO> NotificationDTOList = notificationList.stream().map(notification ->
                NotificationDTO.builder()
                        .message(notification.getMessage())
                        .type(notification.getType())
                        .createdAt(notification.getCreatedAt())
                        .typeSpecificId(notification.getTypeSpecificId())
                        .build()
        ).collect(Collectors.toList());
        return NotificationDTOList;
    }
}
