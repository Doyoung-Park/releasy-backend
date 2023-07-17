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

    private final ReleaseRepository releaseRepository;
    private final MemberRepository memberRepository;
    private final IssueRepository issueRepository;

    public ReleaseNote createRelease(CreateReleaseDTO createReleaseDTO, Member member, Project project) {

        ReleaseNote releaseNote = ReleaseNote.builder()
                .version(createReleaseDTO.getVersion())
                .status(createReleaseDTO.getStatus())
                .percent(createReleaseDTO.getPercent())
                .releaseDate(createReleaseDTO.getReleaseDate())
                .brief(createReleaseDTO.getBrief())
                .description(createReleaseDTO.getDescription())
                .isActive(true)
                .member(member)
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

    public void updateIssues(Long releaseId, List<Issue> newIssueList) {   // issueList : 결과물
        List<Issue> oldIssueListOfReleaseNote = issueRepository.findAllByReleaseNoteId(releaseId);

        // 추가
        for (Issue issue : newIssueList) {
            Long issueId = issue.getId();
            boolean foundInOldList = false;

            for (Issue oldIssue : oldIssueListOfReleaseNote) {
                if (issueId.equals(oldIssue.getId())) {
                    foundInOldList = true;
                    break;
                }
            }

            if (!foundInOldList) {
                Optional<Issue> existingIssue = issueRepository.findById(issueId); // Assuming you have an issueRepository to retrieve the existing issue by its ID
                int res = issueRepository.insertIssueFromReleaseNote(releaseId, existingIssue.get().getId()); // Assuming you have a method addReleaseNote in your Issue class to associate the releaseNote
            }
        }

        // 삭제
        for (Issue issue : oldIssueListOfReleaseNote) {
            Long issueId = issue.getId();
            boolean foundInNewList = false;

            for (Issue newIssue : newIssueList) {
                if (issueId.equals(newIssue.getId())) {
                    foundInNewList = true;
                    break;
                }
            }

            if (!foundInNewList) {
                Optional<Issue> deleteIssue = issueRepository.findById(issueId); // Assuming you have an issueRepository to retrieve the existing issue by its ID
                int res = issueRepository.deleteIssueFromReleaseNote(deleteIssue.get().getId()); // Assuming you have a method addReleaseNote in your Issue class to associate the releaseNote
            }
        }

        return;
    }

    public void deleteRelease(Long id) {
        // 릴리즈 노트 아이디로 isActive를 False로 바꾸고 지운 시간 넣어주기
        releaseRepository.updateIsActiveById(id, new Date());
    }
}
