package kakao99.backend.member.dto;

import kakao99.backend.entity.Member;
import kakao99.backend.entity.Project;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class MemberGroupDTO {
    private String name;
    private String nickname;
    private String email;
    private String groupName;
    private String position;
    private String introduce;
    private String groupCode;
    private List<MemberInfoDTO> groupMember;
}
