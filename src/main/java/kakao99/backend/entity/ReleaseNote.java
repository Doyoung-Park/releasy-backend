package kakao99.backend.entity;

import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "Release_notes")
public class ReleaseNote {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "release_note_id")
    private Long id;

    private String version; // 버전

    private String status;  // 상태

    private Float percent;  // 진행률

    @Column(name = "release_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date releaseDate;   // 배포일

    private String brief;   //  요약

    private String description; //세부 설명

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
    private Date deletedAt; // 삭제일

    @Column(name = "is_active")
    private Boolean isActive; // false: 탈퇴한 회원, true: 탈퇴x 회원도

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    @JsonManagedReference
    @JsonIgnore
    private Project project;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "writer_id")
    @JsonManagedReference
    @JsonIgnore
    private Member member;
}
