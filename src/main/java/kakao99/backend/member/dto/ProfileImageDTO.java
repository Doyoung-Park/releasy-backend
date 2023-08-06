package kakao99.backend.member.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProfileImageDTO {
    private byte[] image;
}
