package kakao99.backend.issue.controller;

import kakao99.backend.common.exception.ErrorCode;
import kakao99.backend.entity.*;

import kakao99.backend.common.exception.CustomException;
import kakao99.backend.issue.dto.DragNDropDTO;
import kakao99.backend.issue.dto.IssueDTO;


import kakao99.backend.issue.dto.ProjectWithIssuesDTO;
import kakao99.backend.issue.repository.IssueParentChildRepository;
import kakao99.backend.issue.repository.IssueRepository;
import kakao99.backend.issue.service.IssueService;
import kakao99.backend.issue.service.TreeService;
import kakao99.backend.member.repository.MemberRepository;
import kakao99.backend.project.repository.ProjectRepository;
import kakao99.backend.common.ResponseMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@Slf4j
public class IssueController {

    private final IssueRepository issueRepository;
    private final IssueParentChildRepository issueParentChildRepository;
    private final IssueService issueService;
    private final TreeService treeService;
    private final MemberRepository memberRepository;
    private final ProjectRepository projectRepository;


    // 이슈 생성
    @PostMapping("/api/project/{projectId}/issue")
        public ResponseEntity<?> createIssue(@RequestBody IssueForm issue, @PathVariable("projectId") Long projectId) {

        Optional<Member> memberById = memberRepository.findById(issue.getUserId());
        
        if (memberById.isEmpty()) {
            ResponseMessage message = new ResponseMessage(404, "해당 userId에 해당하는 유저 데이터 없음.");
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        }

        Optional<Project> projectById = projectRepository.findById(projectId);
        if (projectById.isEmpty()) {
            ResponseMessage message = new ResponseMessage(404, "해당 projectId 해당하는 프로젝트 데이터 없음.");
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        }

        Member member = memberById.get();
        Project project = projectById.get();

        Issue newIssue = new Issue().builder()
                .title(issue.getTitle())
                .issueType(issue.getType())
                .description(issue.getDescription())
                .memberReport(member)
                .memberInCharge(member)
                .status("backlog")
                .project(project)
                .isActive(true)
                .build();


        issueRepository.save(newIssue);
        ResponseMessage message = new ResponseMessage(200, "이슈 생성 성공");
        return new ResponseEntity(message, HttpStatus.OK);
    }


    @PostMapping("/api/{projectId}/issues/{issueId}/childissue")
    public ResponseEntity<?> createChildIssue(@RequestBody ChildIssueForm issue, @PathVariable("projectId") Long projectId,@PathVariable("issueId") Long issueId) {

        Optional<Project> projectById = projectRepository.findById(projectId);

        Optional<Issue> parentById = issueRepository.findById(issue.getParentIssueId());

        Optional<Issue> childById = issueRepository.findById(issue.getChildIssueId());

        if (projectById.isEmpty()) {
            ResponseMessage message = new ResponseMessage(404, "해당 projectId 해당하는 프로젝트 데이터 없음.");
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        }

        if (parentById.isEmpty()) {
            ResponseMessage message = new ResponseMessage(404, "해당 parentById 해당하는 이슈 데이터 없음.");
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        }

        if (childById.isEmpty()) {
            ResponseMessage message = new ResponseMessage(404, "해당 childById 해당하는 이슈 데이터 없음.");
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        }


        Issue parent = parentById.get();
        Issue child = childById.get();



        IssueParentChild newChildIssue = IssueParentChild.createIssueParentChild(parent, child, issue.getCreatedAt());

        issueParentChildRepository.save(newChildIssue);
        ResponseMessage message = new ResponseMessage(200, "하위 이슈 생성 성공");
        return new ResponseEntity(message, HttpStatus.OK);
    }


    @GetMapping("api/{projectId}/issues")
    public ResponseEntity<?> getAllIssues(
            @PathVariable("projectId") Long projectId,
            @RequestParam(value = "status", required = false) String status,
            @RequestParam(value = "type", required = false) String type,
            @RequestParam(value = "username", required = false) String name,
            @RequestParam(value = "exclude",required = false) Long excludeId)
    {


        List<IssueDTO> allIssues = null;

        if(excludeId != null)
        {
            allIssues = issueService.getAllIssuesWithoutexcludeId(projectId,excludeId);
        }
        else {
            allIssues = issueService.getAllIssuesByFilter(projectId, status, type, name);
        }
        ResponseMessage message = new ResponseMessage(200, projectId + "번 프로젝트의 모든 이슈 상태,타입,담당자별 조회 성공", allIssues);

        return new ResponseEntity(message, HttpStatus.OK);
    }

    @GetMapping("/api/{projectId}/issues/tree/{issueId}")
    public ResponseEntity<List<TreeNode>> getWholeTree(@PathVariable("projectId") Long projectId, @PathVariable("issueId") Long issueId) {
        List<TreeNode> wholeTree = treeService.getWholeTreeFromMidNode(issueId);
        return ResponseEntity.ok(wholeTree);
    }





    // 이슈 정보 업데이트
    @PutMapping("/api/project/{projectId}/issues/{issueId}")
    public ResponseEntity<?> updateIssue(@PathVariable("projectId") Long projectId, @PathVariable("issueId") Long issueId, @RequestBody UpdateIssueForm updateIssueForm) {
        issueService.updateIssue(updateIssueForm, issueId);

        ResponseMessage message = new ResponseMessage(200, projectId + "번 프로젝트의 모든 이슈 수정 성공");

        return new ResponseEntity(message, HttpStatus.OK);
    }


    // releaseNote id로 모든 이슈 조회
    @GetMapping("/api/releaseNote/{releaseNoteId}/issues")
    public ResponseEntity<?> getIssueListIncludedInReleaseNote(@PathVariable("releaseNoteId") Long releaseNoteId) {

        List<IssueDTO> allIssuesByReleaseNoteId = issueService.getIssueListIncludedInReleaseNote(releaseNoteId);
        ResponseMessage message = new ResponseMessage(200, "릴리즈 노트의 관련된 이슈 조회 성공", allIssuesByReleaseNoteId);
        return new ResponseEntity(message, HttpStatus.OK);
    }

    @GetMapping("/api/project/{projectId}/issues")
    public ResponseEntity<?> getIssueListNotIncludedInReleaseNote(@PathVariable("projectId") Long projectId) {

        List<IssueDTO> allByNotReleaseNoteId = issueService.getIssueListNotIncludedInReleaseNote(projectId);
        ResponseMessage message = new ResponseMessage(200, "릴리즈 노트에 포함되지 않은 이슈 조회 성공", allByNotReleaseNoteId);
        return new ResponseEntity(message, HttpStatus.OK);
    }



    // issue management 페이지에서 필요한 데이터 get
    @GetMapping("/api/project/{projectId}/issues/management")
    public ResponseEntity<?> getIssueManagementPageData(@PathVariable("projectId") Long projectId) {
        ProjectWithIssuesDTO issueManagementPageData = issueService.getIssueManagementPageData(projectId);
        ResponseMessage message = new ResponseMessage(200, "issue Management 페이지에 필요한 데이터 조회 성공", issueManagementPageData);
        return new ResponseEntity(message, HttpStatus.OK);
    }

    @PostMapping("/api/project/{projectId}/issues/management/dragndrop")
    public ResponseEntity<?> updateIssueByDragNDrop(@PathVariable("projectId") Long projectId, @RequestBody DragNDropDTO dragNDropDTO, Authentication authentication) {
        log.info("드래그앤드랍");
        Member member = (Member) authentication.getPrincipal();
        issueService.updateIssueByDragNDrop(dragNDropDTO, member.getId());

        ResponseMessage message = new ResponseMessage(200, "드래그앤드랍으로 이슈 상태 update 성공");
        return new ResponseEntity(message, HttpStatus.OK);
    }

    // 예외 처리 예시
    @GetMapping("/test/test/{releaseNoteId}")

    public String exceptionExample(@PathVariable("releaseNoteId") Long releaseNoteId) {

        if (releaseNoteId == 0)
            throw new CustomException(5001, "테스트 Exception");

        if (releaseNoteId == 1) {
            throw new CustomException(5001, "테스트 Exception", "IssueController.exceptionExample()");
        }

        if (releaseNoteId == 2) {
            throw new CustomException(ErrorCode.NOT_MATCH_CODE);
        }

        return "Success";
    }

//    @GetMapping("/test/test/project/{projectId}")
//    public List<?> test3(@PathVariable("projectId") Long projectId) {
//
////        System.out.println("userId = " + testForm.getUserId());
//
////        List<?> allByProjectIdImpl = issueRepositoryImpl.findAllByProjectIdImpl(projectId);
//
//
//        return allByProjectIdImpl;
//    }


    @DeleteMapping("/api/issue/{issueId}")
    public ResponseEntity<?> deleteIssue(Authentication authentication, /* @RequestBody Long issueId */ @PathVariable("issueId") Long issueId) {
        System.out.println("issueId = " + issueId);

        Member member = (Member) authentication.getPrincipal();

        issueService.deleteIssue(issueId, member.getId());

        ResponseMessage message = new ResponseMessage(200, issueId + "번이 삭제되었습니다.");
        return new ResponseEntity<>(message, HttpStatus.OK);
    }


}


