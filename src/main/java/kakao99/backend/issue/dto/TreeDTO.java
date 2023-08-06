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
public class TreeDTO {

    private Long id;

    private String title;
    private String issueType;
    private String status;

//    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
//    @JsonIgnore
//    private MemberInfoDTO memberIdInCharge;
//    private MemberInfoDTO memberReport;


    public static TreeDTO fromIssueAndIsChild(Issue issue) {
        TreeDTO treeDTO = TreeDTO.builder()
                .id(issue.getId())

                .title(issue.getTitle())
                .issueType(issue.getIssueType())
                .status(issue.getStatus())
                .build();

        return treeDTO;
    }

}



