package kakao99.backend.issue.controller;


import kakao99.backend.entity.Issue;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class ChildIssueForm {
   private Long parentIssueId;
   private Long childIssueId;
   private Date createdAt;
   private Date updatedAt;
   private Date deletedAt;


}
