package kakao99.backend.memo.dto;


import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class UpdateMemoDTO {
    Long memoId;
    Long memberId;
    String content;
    Date createdAt;
    Date updatedAt;
}
