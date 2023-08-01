package kakao99.backend.entity.types;

import lombok.Getter;

@Getter
public enum NotificationType {
    DRAGNDROP("dragNDrop", "로 상태 변경되었습니다."),
    ALLOCATED("allocated", "이슈를 할당했습니다.");

    private String type;
    private String message;

    NotificationType(String type, String message) {
        this.type = type;
        this.message = message;
    }
}
