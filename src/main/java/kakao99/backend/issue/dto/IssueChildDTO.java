package kakao99.backend.issue.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import kakao99.backend.entity.Issue;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import net.minidev.json.annotate.JsonIgnore;

import java.util.Date;

@Getter
@Setter
@Builder
public class IssueChildDTO {

    private Long id;
    private Integer issueNum;
    private String title;
    private String issueType;
    private String description;
    private String status;
    private Integer importance;
    private Date createdAt;

    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @JsonIgnore
    private MemberInfoDTO memberIdInCharge;
    private MemberInfoDTO memberReport;




    public static IssueChildDTO fromIssue(Issue issue) {
        return IssueChildDTO.builder()
                .id(issue.getId())
                .issueNum(issue.getIssueNum())
                .title(issue.getTitle())
                .issueType(issue.getIssueType())
                .description(issue.getDescription())
                .status(issue.getStatus())
                .importance(issue.getImportance())
                .createdAt(issue.getCreatedAt())
                .memberIdInCharge(MemberInfoDTO.builder()
                        .name(issue.getMemberInCharge().getUsername())
                        .nickname(issue.getMemberInCharge().getNickname())
                        .email(issue.getMemberInCharge().getEmail())
                        .position(issue.getMemberInCharge().getPosition())
                        .build())
                .memberReport(MemberInfoDTO.builder()
                        .name(issue.getMemberReport().getUsername())
                        .nickname(issue.getMemberReport().getNickname())
                        .email(issue.getMemberReport().getEmail())
                        .position(issue.getMemberReport().getPosition())
                        .build())
                .build();
    }


}
