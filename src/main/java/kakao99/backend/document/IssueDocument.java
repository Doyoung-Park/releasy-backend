package kakao99.backend.document;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import kakao99.backend.entity.IssueParentChild;
import kakao99.backend.entity.Member;
import kakao99.backend.entity.Project;
import kakao99.backend.entity.ReleaseNote;
import lombok.*;
import net.minidev.json.annotate.JsonIgnore;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.elasticsearch.annotations.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.springframework.data.elasticsearch.annotations.DateFormat.date_hour_minute_second_millis;
import static org.springframework.data.elasticsearch.annotations.DateFormat.epoch_millis;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Document(indexName = "issue")
@Mapping(mappingPath = "elastic/issue-mapping.json")
@Setting(settingPath = "elastic/issue-setting.json")
public class IssueDocument {

    @Field(name = "member_id_in_charge", type = FieldType.Long)
    private Long memberInCharge;  // 담당자

    @Field(name = "member_id_report", type = FieldType.Long)
    private Long memberReport;    // 이슈 보고자

    @Id
    private Long id;

    @Field(name = "issue_num", type = FieldType.Integer)
    private Integer issueNum;  // 프로젝트별 이슈 변호

    @Field(name = "title", type = FieldType.Text)
    private String title;   // 제목

    @Field(name = "issue_type", type = FieldType.Keyword)
    private String issueType;  // 이슈 타입: 에러, task

    @Field(name = "description", type = FieldType.Text)
    private String description; // 설명

    @Field(name = "status", type = FieldType.Keyword)
    private String status;  // 상태: 백로그, 진행중, 완료 ...

    @Field(name = "list_position", type = FieldType.Integer)
    private Integer listPosition;  // 칸반 내의 순서

    @Field(name = "file", type = FieldType.Text)
    private String file; // 첨부 파일

    @Field(name = "importance", type = FieldType.Integer)
    private Integer importance; // 중요도

    @Field(name = "created_at", type = FieldType.Date)
    private Date createdAt; // 생성일

    @Field(name = "updated_at", type = FieldType.Date)
    private Date updatedAt; // 수정일

    @Field(name = "deleted_at", type = FieldType.Date)
    private Date deletedAt; // 삭제일

    @Field(name = "is_active", type = FieldType.Boolean)
    private Boolean isActive;

    private List<IssueParentChild> childIssues = new ArrayList<>();

    private List<IssueParentChild> parentIssues = new ArrayList<>();


}
