package kakao99.backend.document;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Id;
import lombok.*;
import org.springframework.data.elasticsearch.annotations.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Document(indexName = "member")
@Mapping(mappingPath = "elastic/issue-mapping.json")
@Setting(settingPath = "elastic/member-setting.json")
public class MemberDocument {

    @Id
    private Long id;

    private Long _id;

    private String username;

    private String nickname;

    private String email;

    private String position;

    private String role;

    private Long projectId;

}
