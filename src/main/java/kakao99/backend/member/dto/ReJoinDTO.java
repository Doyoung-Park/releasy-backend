package kakao99.backend.member.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReJoinDTO {

    @Email
    private String email;

    @NotBlank
    private String groupName;

}
