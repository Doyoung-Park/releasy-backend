package kakao99.backend.issue.dto;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import kakao99.backend.entity.Issue;
import lombok.*;
import net.minidev.json.annotate.JsonIgnore;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


@Getter
@Setter
@AllArgsConstructor
@Builder
@RequiredArgsConstructor
public class IssueGrassDTO {
    private String date;
    private Long count;
}