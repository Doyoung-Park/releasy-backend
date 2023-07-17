package kakao99.backend.member.dto;


import kakao99.backend.entity.Project;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberUpdateDTO {

    private String name;
    private String nickname;
    private String position;
    private String introduce;
}
