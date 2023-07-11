package kakao99.backend.group.dto;

import kakao99.backend.entity.Group;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GroupNameDTO {
    private String name;
    private String code;
    public Group toEntity() {
        return Group.builder()
                .name(this.name)
                .build();
    }


}