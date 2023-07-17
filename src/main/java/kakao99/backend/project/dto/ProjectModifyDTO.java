package kakao99.backend.project.dto;

import kakao99.backend.entity.Project;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectModifyDTO {
    private Long id;

    private String name;    // 제목

    private String description; // 설명

    private String status;
}
