package kakao99.backend.issue.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class IssueStatusAndListPositionDTO {
    private String newStatus;
    private Float newListPosition;

}
