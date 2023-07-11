package kakao99.backend.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;
import java.util.Optional;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "Memos")
public class Memo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "memo_id")
    private Long id;

    private String memo_content;    // 메모 내용

    @Column(name = "created_at", nullable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    private Date createdAt; // 생성일

    @Column(name = "updated_at", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @UpdateTimestamp
    private Date updatedAt; // 수정일

    @Column(name = "deleted_at", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @UpdateTimestamp
    private Date deletedAt; // 삭제일

    @Column(name = "is_active")
    private Boolean isActive; // false: 탈퇴한 회원, true: 탈퇴x 회원

    @ManyToOne
    @JoinColumn(name = "issue_id")
    @JsonManagedReference
    private Issue issue;

    @ManyToOne
    @JoinColumn(name = "writer_id")
    @JsonManagedReference
    private Member member;

    public static Memo createMemo(Member member,Issue issue,String memoContent,Date createdAt) {

        return Memo.builder()
                .memo_content(memoContent)
                .isActive(true)  // Assuming memos are active when created
                .issue(issue)
                .member(member)
                .createdAt(createdAt)
                .build();
    }

    public void updateMemo(String memo_content, Date updatedAt){
        this.memo_content = memo_content;
        this.updatedAt = updatedAt;
    }

    public void deleteMemo(){
        this.isActive = false;
        this.deletedAt = new Date();
    }



}
