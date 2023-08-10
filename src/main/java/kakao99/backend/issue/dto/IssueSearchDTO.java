package kakao99.backend.issue.dto;

import kakao99.backend.member.dto.MemberInfoDTO;
import lombok.*;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@Builder
@RequiredArgsConstructor
public class IssueSearchDTO {

    private Long id;

    private Integer issueNum;

    private String title;

    private String issueType;

    private String description;

    private String status;

    private Integer listPosition;

    private Integer importance;

    private String file;

    private Date createdAt;

    private Date updatedAt;

    private String releasenote;

    private MemberInfoDTO memberIdInCharge;

    private MemberInfoDTO memberReport;

    private List<IssueChildDTO> childIssue;

    private List<IssueChildDTO> parentIssue;
}
