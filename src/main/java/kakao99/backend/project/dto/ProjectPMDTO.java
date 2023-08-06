package kakao99.backend.project.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import kakao99.backend.entity.Group;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProjectPMDTO {

    private Long id;

    private String name;    // 제목

    private String description; // 설명

    private String status;  // 진행 상태

    private Date createdAt; // 생성일

    private Date updatedAt; // 수정일

    private Date deletedAt; // 삭제일

    private String isActive;

    private Long groupId;

    private Long PMId;

    private String PMName;
}


