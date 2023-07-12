package kakao99.backend.entity;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import net.minidev.json.annotate.JsonIgnore;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "Members")
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column
    private String username;    // 이름

    @Column
    private String nickname; // 닉네임

    @Column
    @Email
    private String email; // 이메일

    private String password; // 이메일

    @Column
    private String position; // 직무 ex. backend. frontend ...

    @Column
    private String provider; // 소셜 로그인

    @Column
    private String authority; // 권한

    @Column(name = "created_at", nullable = false, updatable = false)
    @Temporal(TemporalType.DATE)
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


    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @JsonIgnore
    @JoinColumn(name = "group_id")
    private Group group;

    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY)
    @JsonBackReference
    private List<ReleaseNote> releaseNoteList;
    public Member updateOAuth(String email, String username) {
        this.email = email;
        this.username = username;

        return this;
    }
}


