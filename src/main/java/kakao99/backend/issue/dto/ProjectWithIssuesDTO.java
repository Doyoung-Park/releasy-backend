package kakao99.backend.issue.dto;

import kakao99.backend.entity.Project;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
public class ProjectWithIssuesDTO {
    private Long id;

    private String name;

    private List<MemberInfoDTO> users; // 담당자

    private List<IssueDTO> issues;  // 이슈 리스트



    public ProjectWithIssuesDTO saveProjectIdAndName(Project project) {
        this.id = project.getId();
        this.name = project.getName();
        this.users = null;
        return this;
    }

    public void saveIssueList(List<IssueDTO> issues) {
        this.issues = issues;
    }

}
