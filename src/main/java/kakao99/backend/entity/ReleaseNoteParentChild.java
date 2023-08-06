package kakao99.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "Releases_parent_child")
public class ReleaseNoteParentChild {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "releasenotes_parent_child_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_note_id")
    private ReleaseNote parentNote;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "child_note_id")
    private ReleaseNote childNote;

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
    private Boolean isActive; // false: 탈퇴한 회원, true: 탈퇴x 회원도



    public static ReleaseNoteParentChild createReleaseNoteParentChild (ReleaseNote parentNote,ReleaseNote childNote,Date createdAt) {

        return ReleaseNoteParentChild.builder()
                .parentNote(parentNote)
                .childNote(childNote)
                .createdAt(createdAt)
                .updatedAt(null)
                .deletedAt(null)
                .isActive(true)  // Assuming memos are active when created
                .build();
    }
}
