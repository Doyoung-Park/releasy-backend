package kakao99.backend.member.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginDTO {

    @Email
    @NotBlank
    private String email;

    @Size(min = 8)
    private String password;
}
