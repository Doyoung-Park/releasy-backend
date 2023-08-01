package kakao99.backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import kakao99.backend.entity.types.NotificationType;
import kakao99.backend.issue.dto.DragNDropDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import net.minidev.json.annotate.JsonIgnore;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "Notifications")
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notification_id")
    private Long id;

    private String updatedIssueStatusBefore; // 변경 이전 status

    private String updatedIssueStatusAfter; // 변경 이후 status

    private String type;    // 알림 유형

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "issue_id")
    @JsonManagedReference
    private Issue issue;

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
    private Boolean isActive; // 삭제 여부

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id_in_charge")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @JsonIgnore
    private Member memberInCharge;  // 담당자

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id_report")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Member memberReport;    // 알림 보고자

    public static Notification createdByDragNDrop(DragNDropDTO dragNDropDTO, Member memberReport, Issue issue) {

        return Notification.builder()
                .memberReport(memberReport)
                .type(NotificationType.DRAGNDROP.getType())// 이슈 타입
                .updatedIssueStatusBefore(dragNDropDTO.getSourceStatus())
                .updatedIssueStatusAfter(dragNDropDTO.getDestinationStatus())
                .issue(issue)
                .createdAt(new Date())
                .updatedAt(null)
                .deletedAt(null)
                .isActive(true)  // Assuming memos are active when created
                .build();
    }
}
