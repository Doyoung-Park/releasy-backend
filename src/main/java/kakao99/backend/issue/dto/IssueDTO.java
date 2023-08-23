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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


@Getter
@Setter
@AllArgsConstructor
@Builder
@RequiredArgsConstructor
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
    private Date updatedAt;
    private String releasenote;
    private List<IssueChildDTO> childIssue;
    private List<IssueChildDTO> parentIssue;
    private String imgUrl_1;
    private String imgUrl_2;
    private String imgUrl_3;

    private boolean isChild;

    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @JsonIgnore
    private MemberInfoDTO memberIdInCharge;
    private MemberInfoDTO memberReport;




    public static List<IssueDTO> getIssueDTOListFromIssueList(List<Issue> allByNotReleaseNoteId) {
        return allByNotReleaseNoteId.stream().map(issue -> {
            // Create the IssueDTO object
            IssueDTO issueDTO = IssueDTO.builder()
                    .id(issue.getId())
                    .issueNum(issue.getIssueNum())
                    .title(issue.getTitle())
                    .issueType(issue.getIssueType())
                    .description(issue.getDescription())
                    .status(issue.getStatus())
                    .file(issue.getFile())
                    .createdAt(issue.getCreatedAt())
                    .updatedAt(issue.getUpdatedAt())
                    .imgUrl_1(issue.getImgUrl_1())
                    .imgUrl_2(issue.getImgUrl_2())
                    .imgUrl_3(issue.getImgUrl_3())
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
                    .build();

            List<IssueChildDTO> childIssueDTOs = issue.getChildIssues().stream()
                    .filter(issueParentChild -> issueParentChild.getIsActive()) // Apply the filter here
                    .map(issueParentChild -> IssueChildDTO.fromIssue(issueParentChild.getChildIssue()))
                    .collect(Collectors.toList());

            // Add the childIssueDTOs to the issueDTO
            issueDTO.setChildIssue(childIssueDTOs);

            return issueDTO;
        }).collect(Collectors.toList());
    }

    public static IssueDTO fromIssueAndIsChild(Issue issue, boolean isChild) {
        IssueDTO issueDTO = IssueDTO.builder()
                .id(issue.getId())
                .issueNum(issue.getIssueNum())
                .title(issue.getTitle())
                .issueType(issue.getIssueType())
                .description(issue.getDescription())
                .status(issue.getStatus())
                .file(issue.getFile())
                .createdAt(issue.getCreatedAt())
                .updatedAt(issue.getUpdatedAt())
                .listPosition(issue.getListPosition())
                .imgUrl_1(issue.getImgUrl_1())
                .imgUrl_2(issue.getImgUrl_2())
                .imgUrl_3(issue.getImgUrl_3())
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
                .isChild(isChild)
                .build();

        List<IssueChildDTO> childIssueDTOs = issue.getChildIssues().stream()
                .filter(issueParentChild -> issueParentChild.getIsActive()) // Apply the filter here
                .map(issueParentChild -> IssueChildDTO.fromIssue(issueParentChild.getChildIssue()))
                .collect(Collectors.toList());

        List<IssueChildDTO> parentIssueDTOs = issue.getParentIssues().stream()
                .filter(issueParentChild -> issueParentChild.getIsActive()) // Apply the filter here
                .map(issueParentChild -> IssueChildDTO.fromIssue(issueParentChild.getParentIssue()))
                .collect(Collectors.toList());


        // Add the childIssueDTOs to the issueDTO
        issueDTO.setChildIssue(childIssueDTOs);
        issueDTO.setParentIssue(parentIssueDTOs);
        return issueDTO;
    }

}



