package kakao99.backend.issue.service;

import kakao99.backend.document.IssueDocument;
import kakao99.backend.document.MemberDocument;
import kakao99.backend.entity.Issue;
import kakao99.backend.entity.Member;
import kakao99.backend.issue.dto.IssueDTO;
import kakao99.backend.issue.dto.IssueSearchDTO;

import kakao99.backend.issue.repository.IssueRepository;
import kakao99.backend.issue.repository.IssueRepositoryImpl;
import kakao99.backend.issue.repository.IssueSearchRepository;
import kakao99.backend.issue.repository.MemberDocumentRepository;
import kakao99.backend.member.dto.MemberInfoDTO;
import kakao99.backend.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class IssueSearchService {

    private final IssueSearchRepository issueSearchRepository;
    private final MemberDocumentRepository memberDocumentRepository;
    private final IssueRepository issueRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public List<IssueSearchDTO> issueSearch(String title) {

        List<IssueDocument> issueDocumentByTitle = issueSearchRepository.findIssueDocumentByTitleContaining(title);
        //return issueSearchRepository.findIssueDocumentByTitle(title);


        return convertToIssueSearchDTOList(issueDocumentByTitle);

    }

    private List<IssueSearchDTO> convertToIssueSearchDTOList(List<IssueDocument> issueDocumentByTitle) {
        return issueDocumentByTitle.stream()
                .map(issueDocument -> IssueSearchDTO.builder()
                        .id(issueDocument.getId())
                        .issueNum(issueDocument.getIssueNum())
                        .title(issueDocument.getTitle())
                        .issueType(issueDocument.getIssueType())
                        .description(issueDocument.getDescription())
                        .status(issueDocument.getStatus())
                        .listPosition(issueDocument.getListPosition())
                        .importance(issueDocument.getImportance())
                        .file(issueDocument.getFile())
                        .createdAt(issueDocument.getCreatedAt())
                        .updatedAt(issueDocument.getUpdatedAt())
                        .memberIdInCharge(convertMemberInfoDTO(issueDocument.getMemberInCharge()))
                        .memberReport(convertMemberInfoDTO(issueDocument.getMemberReport()))
                        .childIssue(null)
                        .parentIssue(null)
                        .build())
                .collect(Collectors.toList());
    }

    private MemberInfoDTO convertMemberInfoDTO(Long id) {

        Optional<MemberDocument> memberDocumentById = memberDocumentRepository.findMemberDocumentBy_id(id);

        MemberDocument memberDocument = memberDocumentById.get();

        return MemberInfoDTO.builder()
                .id(memberDocument.get_id())
                .name(memberDocument.getUsername())
                .nickname(memberDocument.getNickname())
                .email(memberDocument.getEmail())
                .position(memberDocument.getPosition())
                .role(memberDocument.getRole())
                .build();
    }

    @Transactional
    public void saveMillionIssue(Long memberId) {

        Optional<Member> byId = memberRepository.findById(1L);
        Member member = byId.get();
        for (int i = 1; i < 10000; i++) {
            Issue issue = Issue.builder()
                    .issueNum(i)              // 이슈 번호
                    .title("이슈 테스트 " + i)      // 제목
                    .issueType("task")          // 이슈 타입
                    .description("This is a sample issue.")  // 설명
                    .status("backlog")          // 상태
                    .listPosition(1)            // 칸반 내의 순서
                    .file("sample.pdf")         // 첨부 파일
                    .importance(3)             // 중요도
                    .createdAt(new Date())     // 생성일
                    .updatedAt(new Date())     // 수정일
                    .deletedAt(null)           // 삭제일
                    .isActive(true)            // 활성 여부
                    .memberInCharge(member)  // 담당자 (새 Member 객체로 가정)
                    .memberReport(member)    // 이슈 보고자 (새 Member 객체로 가정)
                    .project(null)        // 프로젝트 (새 Project 객체로 가정)
                    .releaseNote(null)  // 릴리스 노트 (새 ReleaseNote 객체로 가정)
                    .childIssues(null)  // 자식 이슈 목록 (빈 ArrayList로 가정)
                    .parentIssues(null) // 부모 이슈 목록 (빈 ArrayList로 가정)
                    .build();

            issueRepository.save(issue);
        }
    }

    @Transactional
    public List<IssueDTO> getIssueListMySQL(String title) {
        List<Issue> issueList = issueRepository.findIssueByTitleContaining(title);
        return IssueDTO.getIssueDTOListFromIssueList(issueList);

    }
}
