package kakao99.backend.release.dto;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import com.querydsl.core.annotations.QueryProjection;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import kakao99.backend.entity.ReleaseNote;
import kakao99.backend.entity.Member;
import kakao99.backend.entity.Memo;


import lombok.*;
import net.minidev.json.annotate.JsonIgnore;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


@Getter
@Setter
@AllArgsConstructor
@Builder
@RequiredArgsConstructor
public class NoteTreeDTO {

    private Long id;

    private String version;



    private boolean isActive;

//    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
//    @JsonIgnore
//    private MemberInfoDTO memberIdInCharge;
//    private MemberInfoDTO memberReport;


    public static NoteTreeDTO fromNoteAndIsChild(ReleaseNote releaseNote) {
        NoteTreeDTO treeDTO = NoteTreeDTO.builder()
                .id(releaseNote.getId())
                .version(releaseNote.getVersion())
                .isActive(releaseNote.getIsActive())
                .build();

        return treeDTO;
    }


}



