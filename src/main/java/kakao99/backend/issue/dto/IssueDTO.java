package kakao99.backend.issue.dto;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import com.querydsl.core.annotations.QueryProjection;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import kakao99.backend.entity.Issue;
import kakao99.backend.entity.Member;
import kakao99.backend.entity.Memo;

import lombok.*;
import net.minidev.json.annotate.JsonIgnore;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


@Getter
@Setter
@AllArgsConstructor
@Builder
public class IssueDTO {

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

    private String releasenote;


    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @JsonIgnore
    private MemberInfoDTO memberIdInCharge;
    private MemberInfoDTO memberReport;


    public static List<IssueDTO> getIssueDTOListFromIssueList(List<Issue> allByNotReleaseNoteId) {
        List<IssueDTO> issueDTOList = allByNotReleaseNoteId.stream().map(issue -> IssueDTO.builder()
                .id(issue.getId())
                .issueNum(issue.getIssueNum())
                .title(issue.getTitle())
                .issueType(issue.getIssueType())
                .description(issue.getDescription())
                .status(issue.getStatus())
                .file(issue.getFile())
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
                .importance(issue.getImportance())
                .build()).collect(Collectors.toList());
        return issueDTOList;
    }
}


