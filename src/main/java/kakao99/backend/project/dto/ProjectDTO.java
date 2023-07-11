package kakao99.backend.project.dto;

import kakao99.backend.entity.Group;
import kakao99.backend.entity.Project;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectDTO {
    private String name;    // 제목

    private String description; // 설명

    private Long group_id;

}
