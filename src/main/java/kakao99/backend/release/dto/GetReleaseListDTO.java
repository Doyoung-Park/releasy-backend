package kakao99.backend.release.dto;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotBlank;
import kakao99.backend.entity.Issue;
import kakao99.backend.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GetReleaseListDTO {
    @NotBlank
    private Long id;
    @NotBlank
    private String version; // 버전
    @NotBlank
    private String status;  // 상태
    @NotBlank
    private Date releaseDate;   // 배포일
    @NotBlank
    private Date createdAt; // 생성일
    @NotBlank
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Member member; // 작성자
}
