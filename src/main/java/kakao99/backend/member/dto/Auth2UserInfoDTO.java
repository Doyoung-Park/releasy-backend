package kakao99.backend.member.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Auth2UserInfoDTO {

    private String name;
    private String emil;

    public Auth2UserInfoDTO(String oAuthUserName, String oAuthEmail) {
        this.name = oAuthUserName;
        this.emil = oAuthEmail;
    }
}
