package kakao99.backend.notification.rabbitmq.controller;


import kakao99.backend.notification.rabbitmq.dto.RequestMessageDTO;
import kakao99.backend.notification.rabbitmq.service.MessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
public class MessageController {

    private final MessageService messageService;

    /**
     * Queue로 메시지를 발행
     *
     * @param requestMessageDto 발행할 메시지의 DTO 객체
     * @return ResponseEntity 객체로 응답을 반환
     */
    @RequestMapping(value = "/send/message", method = RequestMethod.POST)
    public ResponseEntity<?> sendMessage(@RequestBody RequestMessageDTO requestMessageDto) {
        messageService.sendMessage(requestMessageDto);
        return ResponseEntity.ok("Message sent to RabbitMQ!");
    }

}