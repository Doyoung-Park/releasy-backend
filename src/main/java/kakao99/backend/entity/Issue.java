package kakao99.backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import net.minidev.json.annotate.JsonIgnore;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "Issues")
public class Issue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "issue_id")
    private Long id;

    private Integer issueNum;  // 프로젝트별 이슈 변호

    private String title;   // 제목

    private String issueType;  // 이슈 타입: 에러, task

    private String description; // 설명

    private String status;  // 상태: 백로그, 진행중, 완료 ...

    private Integer listPosition;  // 칸반 내의 순서

    private String file; // 첨부 파일

    private Integer importance; // 중요도

    private String imgUrl_1;
    private String imgUrl_2;
    private String imgUrl_3;

    @Column(name = "created_at", nullable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    private Date createdAt; // 생성일

    @Column(name = "updated_at")
    @Temporal(TemporalType.TIMESTAMP)
    @UpdateTimestamp
    private Date updatedAt; // 수정일

    @Column(name = "deleted_at")
    @Temporal(TemporalType.TIMESTAMP)
    @UpdateTimestamp
    private Date deletedAt; // 삭제일

    @Column(name = "is_active")
    private Boolean isActive;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id_in_charge")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @JsonIgnore
    private Member memberInCharge;  // 담당자


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id_report")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Member memberReport;    // 이슈 보고자

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private Project project;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "release_note_id")
    @JsonIgnore
    private ReleaseNote releaseNote;

    @OneToMany(mappedBy = "parentIssue", fetch = FetchType.LAZY)
    private List<IssueParentChild> childIssues = new ArrayList<>();

    @OneToMany(mappedBy = "childIssue", fetch = FetchType.LAZY)
    private List<IssueParentChild> parentIssues = new ArrayList<>();


    public Issue deleteReleaseNote() {
        this.releaseNote = null;
        return this;
    }



}
