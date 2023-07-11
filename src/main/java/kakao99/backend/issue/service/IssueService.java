package kakao99.backend.issue.service;

import kakao99.backend.entity.Issue;
import kakao99.backend.entity.Member;
import kakao99.backend.issue.dto.IssueDTO;
import kakao99.backend.issue.dto.MemberInfoDTO;
import kakao99.backend.issue.repository.IssueRepository;
import kakao99.backend.project.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class IssueService {
    private final IssueRepository issueRepository;

    public List<Issue> getIssuesWithMemo(Long projectId) {
        return issueRepository.findAllByProjectId(projectId);
    }


    public ArrayList<IssueDTO> getAllIssues(Long projectId) {
        List<Issue> allIssueByProjectId = issueRepository.findAllByProjectId(projectId);
        ArrayList<IssueDTO> issueDTOList = new ArrayList<>();

        for (Issue issue : allIssueByProjectId) {

            Member memberInCharge = issue.getMemberInCharge();
            MemberInfoDTO memberInfoDTO = MemberInfoDTO.builder()
                    .name(memberInCharge.getUsername())
                    .nickname(memberInCharge.getNickname())
                    .email(memberInCharge.getEmail())
                    .position(memberInCharge.getPosition())
                    .build();

            IssueDTO issueDTO = IssueDTO.builder()
                    .id(issue.getId())
                    .issueNum(issue.getIssueNum())
                    .title(issue.getTitle())
                    .issueType(issue.getIssueType())
                    .description(issue.getDescription())
                    .status(issue.getStatus())
                    .file(issue.getFile())
                    .createdAt(issue.getCreatedAt())
                    .memberIdInCharge(memberInfoDTO)
                    .build();

            issueDTOList.add(issueDTO);
        }
    return issueDTOList;
    }

    public String updateIssue(String title, String description, Long issueId) {

        System.out.println("title = " + title);
        if (title==null && description != null) {
            issueRepository.updateIssueDescription(description, issueId);
        }
        else if (title != null && description==null) {
            issueRepository.updateIssueTitle(title, issueId);
        }else if (title!= null && description != null){
            issueRepository.updateIssue(title, description, issueId);
        }else{
            return "파라미터 전달되지 않음.";
        }
        return "OK";
    }


    public ArrayList<IssueDTO> getAllIssuesByReleaseNoteId(Long releaseNoteId) {
        List<Issue> allIssuesByReleaseNoteId = issueRepository.findAllByReleaseNoteId(releaseNoteId);
        ArrayList<IssueDTO> issueDTOList = new ArrayList<>();

        for (Issue issue : allIssuesByReleaseNoteId) {

            Member memberInCharge = issue.getMemberInCharge();
            MemberInfoDTO memberInfoDTO = MemberInfoDTO.builder()
                    .name(memberInCharge.getUsername())
                    .nickname(memberInCharge.getNickname())
                    .email(memberInCharge.getEmail())
                    .position(memberInCharge.getPosition())
                    .build();

            IssueDTO issueDTO = IssueDTO.builder()
                    .id(issue.getId())
                    .issueNum(issue.getIssueNum())
                    .title(issue.getTitle())
                    .issueType(issue.getIssueType())
                    .description(issue.getDescription())
                    .status(issue.getStatus())
                    .file(issue.getFile())
                    .createdAt(issue.getCreatedAt())
                    .memberIdInCharge(memberInfoDTO)
                    .build();

            issueDTOList.add(issueDTO);
        }
        return issueDTOList;
    }
}
