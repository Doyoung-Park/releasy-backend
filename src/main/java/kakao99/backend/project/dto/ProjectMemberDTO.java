package kakao99.backend.project.dto;

import kakao99.backend.entity.Group;
import kakao99.backend.entity.Project;
import lombok.*;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProjectMemberDTO {
    private String name;    // 제목

    private String description; // 설명

    private String status;

    private Date createdAt;
}
