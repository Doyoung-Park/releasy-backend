package kakao99.backend.issue.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DragNDropDTO {
    private Long issueId;
    private String destinationStatus;
    private String sourceStatus;
}
