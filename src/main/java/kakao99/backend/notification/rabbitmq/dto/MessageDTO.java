package kakao99.backend.notification.rabbitmq.dto;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class MessageDTO {

    private String title;
    private String content;
}
