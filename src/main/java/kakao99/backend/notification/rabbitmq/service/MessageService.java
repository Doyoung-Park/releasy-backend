package kakao99.backend.notification.rabbitmq.service;

import kakao99.backend.notification.rabbitmq.dto.MessageDTO;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
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

    /**
     * Queue로 메시지를 발행
     *
     * @param messageDto 발행할 메시지의 DTO 객체
     */
    public void sendMessage(MessageDTO messageDto) {
        log.info("message sent: {}", messageDto.toString());
        rabbitTemplate.convertAndSend(exchangeName, routingKey, messageDto);
    }

    /**
     * Queue에서 메시지를 구독
     *
     * @param messageDto 구독한 메시지를 담고 있는 MessageDto 객체
     */
    @RabbitListener(queues = "${rabbitmq.queue.name}")
    public void receiveMessage(MessageDTO messageDto) {
        log.info("Received message: {}", messageDto.toString());
        String uuid = rabbitTemplate.getUUID();
        System.out.println("uuid = " + uuid);
    }
}