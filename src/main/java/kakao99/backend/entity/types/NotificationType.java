package kakao99.backend.entity.types;

import lombok.Getter;

@Getter
public enum NotificationType {
    ISSUECREATED("issue", "' 에 의해 발행되었습니다."),
    ISSUEDONE("issue", "' 이슈가 해결되었습니다."),
    ISSUEDELETED("issue", "' 이슈가 삭제되었습니다."),
    ISSUEALLOCATED("issue", "' 에게 할당되었습니다."),
    ISSUECHANGED("issue", "' (으)로 변경되었습니다."),

    RELEASENOTECREATED("releaseNote", " 릴리즈 노트가 생성되었습니다."),
    RELEASENOTECHANGED("releaseNote", " 릴리즈 노트가 수정되었습니다."),
    RELEASENOTEDELETED("releaseNote", " 릴리즈 노트가 삭제되었습니다."),

    NEWMEMBER("member", "' (이)가 참여하였습니다."),
    OUTMEMBER("member","' (이)가 탈퇴하였습니다.");

    private String type;
    private String verb;

    NotificationType(String type, String verb) {
        this.type = type;
        this.verb = verb;
    }
}
