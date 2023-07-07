package kakao99.backend.issue.cotroller;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class IssueForm {
    private String title;   // 이슈 제목
    private String writerName;  // 작성자 이름
    private String type;    // 이슈 타입
    private String description; // 이슈 설명

}
