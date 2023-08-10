package kakao99.backend.release.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import kakao99.backend.common.exception.CustomException;
import kakao99.backend.entity.*;
import kakao99.backend.issue.repository.IssueParentChildRepository;
import kakao99.backend.release.dto.CreateReleaseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;


@Repository
@RequiredArgsConstructor
public class ReleaseRepositoryImpl implements ReleaseRepositoryCustom{
    private final EntityManager em;

    private final IssueParentChildRepository issueParentChildRepository;

    private final JPAQueryFactory query;
    private QIssue issue = QIssue.issue;
    private QProject project = QProject.project;

    private QReleaseNote releaseNote = QReleaseNote.releaseNote;

    private QIssueParentChild issueParentChild = QIssueParentChild.issueParentChild;

    private QMember member = QMember.member;



    public ReleaseNote createReleaseNoteWithImage(CreateReleaseDTO createReleaseDTO, Member member, Project project, ArrayList<String> imgUrlList) {
        ReleaseNote.ReleaseNoteBuilder releaseNoteBuilder = ReleaseNote.builder()
                .version(createReleaseDTO.getVersion())
                .status(createReleaseDTO.getStatus())
                .percent(createReleaseDTO.getPercent())
                .releaseDate(createReleaseDTO.getReleaseDate())
                .brief(createReleaseDTO.getBrief())
                .description(createReleaseDTO.getDescription())
                .isActive(true)
                .member(member)
                .project(project)
                .deletedAt(null);

        if (imgUrlList.size() == 1) {
            releaseNoteBuilder
                    .imgUrl_1(imgUrlList.get(0))
                    .imgUrl_2(null)
                    .imgUrl_3(null);

        } else if (imgUrlList.size() == 2) {
            releaseNoteBuilder
                    .imgUrl_1(imgUrlList.get(0))
                    .imgUrl_2(imgUrlList.get(1))
                    .imgUrl_3(null);
        } else if (imgUrlList.size() == 3) {
            releaseNoteBuilder
                    .imgUrl_1(imgUrlList.get(0))
                    .imgUrl_2(imgUrlList.get(1))
                    .imgUrl_3(imgUrlList.get(2));

        } else {
            throw new CustomException(500, "릴리즈노트 생성 실패");
        }

        ReleaseNote newReleaseNote = releaseNoteBuilder.build();


        return newReleaseNote;
    }
}
