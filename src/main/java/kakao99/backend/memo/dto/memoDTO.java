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


    private Long issueId;

    private String memo_content;

    private String memberNickname;

//    private String title;

//    private String description;
    private String memberNickName;


    private Date createdAt;

    private Date updatedAt;

    public static memoDTO MemoDTO(Long memoId, Long issueId, String memberNickname
                            ,String memoContent,Date createdAt,Date updatedAt ) {


        return memoDTO.builder()
                .id(memoId)
                .issueId(issueId)
                .memberNickname(memberNickname)
                .memo_content(memoContent)
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .build();
    }
}
