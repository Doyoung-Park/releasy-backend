package kakao99.backend.project.dto;

import kakao99.backend.entity.Group;
import kakao99.backend.issue.dto.MemberInfoDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProjectIdDTO {
    private Long id;

    private String name;    // 제목

    private String description; // 설명

    private String status;

    private Date createAt;

    private String groupName;

    private String groupCode;

    private List<MemberInfoDTO> memberInfoDTOList;
}