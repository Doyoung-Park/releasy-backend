package kakao99.backend.issue.controller;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IssueForm {
    private String title;   // 이슈 제목
    private String memberInCharge;  // 할당한 담당자 이름 (null이면 아직 할당 x)
    private String type;    // 이슈 타입
    private String description; // 이슈 설명

    private Long userId;
}
