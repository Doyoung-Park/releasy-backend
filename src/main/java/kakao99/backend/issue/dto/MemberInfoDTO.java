package kakao99.backend.issue.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class MemberInfoDTO {

    private Long id;
    private String name;
    private String nickname;
    private String email;
    private String position;
    private String role;

//    public MemberInfoDTO(String name, String nickname, String email, String position) {
//        this.name = name;
//        this.name = nickname;
//        this.email = email;
//        this.position = position;
//    }

}