package kakao99.backend.issue.controller;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateIssueForm {
    private String title;
    private String description;
    private String status;

    private String issueType;

}
