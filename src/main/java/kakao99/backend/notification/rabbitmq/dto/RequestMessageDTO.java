package kakao99.backend.notification.rabbitmq.dto;

import kakao99.backend.entity.types.NotificationType;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class RequestMessageDTO {
    private NotificationType type;
    private Long specificTypeId;
    private Long projectId;
    private String myNickname;
    private Long objectNickname;


//    private String title;
//    private String content;
}
