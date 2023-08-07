package kakao99.backend.notification.rabbitmq.service;

import kakao99.backend.entity.Notification;
import kakao99.backend.notification.rabbitmq.dto.RequestMessageDTO;
import kakao99.backend.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class MessageService {

    @Value("${rabbitmq.exchange.name}")
    private String exchangeName;

    @Value("${rabbitmq.routing.key}")
    private String routingKey;

    private final RabbitTemplate rabbitTemplate;

    private NotificationService notificationService;
    String testUserId = "990";

    public String getTestUserIdQueue() {
        return "user." + testUserId;
    }

    /**
     * Queue로 메시지를 발행
     *
     * @param requestMessageDto 발행할 메시지의 DTO 객체
     */
    public void sendMessage(RequestMessageDTO requestMessageDto) {
        log.info("message sent: {}", requestMessageDto.toString());
        rabbitTemplate.convertAndSend(exchangeName, routingKey, requestMessageDto);
    }

    /**
     * Queue에서 메시지를 구독
     *
     * @param requestMessageDto 구독한 메시지를 담고 있는 MessageDto 객체
     */
    @RabbitListener(queues = "${rabbitmq.queue.name}")
    public void receiveMessage(RequestMessageDTO requestMessageDto) {
        log.info("Received message: {}", requestMessageDto.toString());
    }

    public void requestCreateNotification(RequestMessageDTO requestMessageDTO) {
        rabbitTemplate.convertAndSend(exchangeName, routingKey, requestMessageDTO);
    }

    @RabbitListener(queues = "${rabbitmq.queue.name}")
    public void createNotification(RequestMessageDTO requestMessageDto) {
        log.info("Received message: {}", requestMessageDto.toString());
    }

//    @RabbitListener(queues = "user."+"#{getTestUserId()}")
//    public void testConsumer(String text) {
//
//        log.info("Test Received message: {}", text);
//    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue,
            exchange = @Exchange(value = "notification.exchange"),
            key = "user.993"    // 이렇게 하면 또 받아와짐.
    ))
    public void testUserQueue(String testMessage) {
        System.out.println("testMessage = " + testMessage);
        log.info("유저가 받은 message: {}", testMessage);
    }

//    @RabbitListener(queues = "user_signup_queue")
//    public void user_signup_queue(String newUserId) {
//        System.out.println("newUserId = " + newUserId);
//    }
}

