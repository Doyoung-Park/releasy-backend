package kakao99.backend.notification.rabbitmq.test;

import kakao99.backend.common.ResponseMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TestController {

    private final RabbitTemplate rabbitTemplate;
    @Value("${rabbitmq.exchange.name}")
    private String exchangeName;

    @GetMapping("test/new/member")
    public ResponseEntity<?> testCreateNewMember() {


        /*
        유저 생성 로직
         */

        String newUserId = "999";

        // 새로운 사용자 가입 이벤트를 "user_signup_queue" 큐로 보냄
        rabbitTemplate.convertAndSend("user_signup_queue", newUserId);

        ResponseMessage message = new ResponseMessage(200, "테스트 유저 생성");

        return new ResponseEntity(message, HttpStatus.OK);
    }


    @GetMapping("test/new/notification")
    public ResponseEntity<?> newNotification() {
        String userId = "999";

        rabbitTemplate.convertAndSend(exchangeName, "user." + userId, "새로운 Noti 추가됨");
        ResponseMessage message = new ResponseMessage(200, "New Notification 생성 완료");

        return new ResponseEntity(message, HttpStatus.OK);
    }

}
