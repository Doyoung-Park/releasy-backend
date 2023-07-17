package kakao99.backend.memo.dto;



import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class CreateMemoDTO {
    Long memberId;
    Long issueId;
    String content;
    Date createdAt;
}
