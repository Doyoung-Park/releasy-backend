package kakao99.backend.memo.dto;

import java.util.Date;



import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class memoDTO {


    private Long id;

    private Long memberId;

    private Long issueId;

    private String memberNickname;

    private String title;

    private String description;

    private String memoContent;

    private Date createdAt;

    private Date updatedAt;

    public static memoDTO MemoDTO(Long memoId,Long memberId, Long issueId, String memberNickname,String title,
                                  String description,String memoContent,Date createdAt,Date updatedAt ) {


        return memoDTO.builder()
                .id(memoId)
                .memberId(memberId)
                .issueId(issueId)
                .memberNickname(memberNickname)
                .title(title)
                .description(description)
                .memoContent(memoContent)
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .build();
    }
}
