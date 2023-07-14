package kakao99.backend.release.service;

import kakao99.backend.entity.*;
import kakao99.backend.issue.repository.IssueRepository;
import kakao99.backend.member.repository.MemberRepository;
import kakao99.backend.release.dto.CreateReleaseDTO;
import kakao99.backend.release.repository.ReleaseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReleaseService {

    private ReleaseRepository releaseRepository;
    private MemberRepository memberRepository;
    private IssueRepository issueRepository;
    private final ReleaseNoteRepository releaseNoteRepository;


    public ReleaseNote createRelease(CreateReleaseDTO CreateReleaseDTO, Member member, Project project) {

        Member member1 = memberRepository.findById(member.getId()).get();
        ReleaseNote releaseNote = ReleaseNote.builder()
                .version(CreateReleaseDTO.getVersion())
                .status(CreateReleaseDTO.getStatus())
                .percent(CreateReleaseDTO.getPercent())
                .releaseDate(CreateReleaseDTO.getReleaseDate())
                .brief(CreateReleaseDTO.getBrief())
                .description(CreateReleaseDTO.getDescription())
                .isActive(true)
                .member(member1)
                .project(project)
                .deletedAt(null)
                .build();

        return releaseRepository.save(releaseNote);
    }

    public List<ReleaseNote> findRelease(Long id) {
        // 해당 프로젝트 id에 맞는 릴리즈 노트 목록 가져오기
        return releaseRepository.findByProjectIdAndIsActiveTrue(id);
    }

    public Optional<ReleaseNote> getReleaseInfo(Long id) {
        // 릴리즈 노트 하나 선택하면 해당하는 릴리즈 노트 정보 가져오기
        return releaseRepository.findById(id);
    }

    public void updateRelease(Long id, String version, String status, Float percent, Date releaseDate, String brief, String description) {
        releaseRepository.updateReleaseNoteById(id, version, status, percent, releaseDate, brief, description);
    }

    public void updateIssues(Long projectId, Long releaseId, List<Issue> newIssueList) {   // issueList : 결과물
        List<Issue> oldIssueListOfReleaseNote = issueRepository.findAllByReleaseNoteId(releaseId);

        ReleaseNote releaseNote = releaseRepository.findReleaseNoteById(releaseId);

        for (int i = 0; i < oldIssueListOfReleaseNote.size(); i++) {
            for (int j = 0; j < newIssueList.size(); j++) {
                if (oldIssueListOfReleaseNote.get(i) != newIssueList.get(j)) {
                    Issue removedIssue = newIssueList.get(j);
                    removedIssue.deleteReleaseNote();
                }
            }
        }


        for (int i = 0; i < newIssueList.size(); i++) {
            for (int j = 0; j < oldIssueListOfReleaseNote.size(); j++) {
                if (newIssueList.get(i) != oldIssueListOfReleaseNote.get(j)) {
                    Issue newIssue = oldIssueListOfReleaseNote.get(j);
                    newIssue.addReleaseNote(releaseNote);
                }
            }
        }

        return;


    }


    public void deleteRelease(Long id) {
        // 릴리즈 노트 아이디로 isActive를 False로 바꾸고 지운 시간 넣어주기
        releaseRepository.updateIsActiveById(id, new Date());
    }
}
