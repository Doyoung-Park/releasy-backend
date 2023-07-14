package kakao99.backend.release.dto;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateReleaseDTO {
    @NotBlank
    private String version; // 버전
    @NotBlank
    private String status;  // 상태
    @NotBlank
    private Float percent;  // 진행률
    @NotBlank
    private Date releaseDate;   // 배포일
    private String brief;   //  요약
    private String description; //세부 설명

    private Long memberId;
    private Long projectId;
}
