package kakao99.backend.group.dto;

import jakarta.validation.constraints.Null;
import kakao99.backend.entity.Group;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GroupDTO {
    private String name;
    private String code;
    public Group toEntity() {
        return Group.builder()
                .name(this.name)
                .isActive("true")
                .deletedAt(null)
                .code(UUID.randomUUID().toString())
                .build();
    }
}