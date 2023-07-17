package kakao99.backend.member.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterDTO {

    @NotBlank
    private String name;

    @NotBlank
    private String nickname;

    private String provider;

    @Email
    private String email;

    @Size(min = 8)
    private String password;

    @NotBlank
    private String position;

    @NotBlank
    private String groupName;

}
