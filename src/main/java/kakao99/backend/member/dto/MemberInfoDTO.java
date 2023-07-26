package kakao99.backend.member.dto;

import kakao99.backend.entity.Project;
import lombok.*;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class MemberInfoDTO {
    private Long id;
    private String name;
    private String nickname;
    private String email;
    private String groupName;
    private String position;
    private String introduce;
    private String authority;
    private Date createdAt;
    private List<Project> projectList;
    private String role;
}
