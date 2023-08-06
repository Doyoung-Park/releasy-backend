package kakao99.backend.notification.repository;

import kakao99.backend.notification.rabbitmq.dto.RequestMessageDTO;

public interface NotificationRepositoryCustom {
    void createNotification(RequestMessageDTO requestMessageDTO);
}
