package kakao99.backend.issue.dto;

import kakao99.backend.entity.Issue;
import lombok.*;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@Builder
public class GPTsaveDTO {
    private Long id;
    private Integer importance;

    public GPTsaveDTO() {}

    public GPTsaveDTO(Long id, Integer importance) {
        this.id = id;
        this.importance = importance;
    }
}
