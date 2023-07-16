package kakao99.backend.issue.test;

import kakao99.backend.entity.Issue;
import kakao99.backend.issue.exception.CustomException;
import kakao99.backend.issue.repository.IssueRepository;
import kakao99.backend.issue.repository.IssueRepositoryImpl;
import kakao99.backend.issue.service.IssueService;
import kakao99.backend.member.repository.MemberRepository;
import kakao99.backend.project.repository.ProjectRepository;
import kakao99.backend.utils.ResponseMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@Slf4j
public class TestController {


    private final IssueRepository issueRepository;
    private final TestService testService;
    private final IssueRepositoryImpl issueRepositoryImpl;
    private final IssueService issueService;
    private final MemberRepository memberRepository;
    private final ProjectRepository projectRepository;


    @GetMapping("/test/test/{issueId}/delete/{releaseNoteId}")
    public String test23(@PathVariable("issueId") Long issueId, @PathVariable("releaseNoteId") Long releaseNoteId) {

        System.out.println("issueId = " + issueId);
//        System.out.println("userId = " + testForm.getUserId());

        int i = issueRepository.updateTest(releaseNoteId, issueId);
        System.out.println("i = " + i);


        return "success";
    }

    @GetMapping("/test/test/{releaseNoteId}")
    public List<Issue> test2(@PathVariable("releaseNoteId") Long releaseNoteId) {

//        System.out.println("userId = " + testForm.getUserId());

        List<Issue> allIssuesByReleaseNoteId = issueRepository.getAllIssuesByReleaseNoteId(releaseNoteId);


        return allIssuesByReleaseNoteId;
    }


//    @GetMapping("/my/test/issue/{issueId}")
    public ResponseEntity<?> optionalTestOrElseGet(@PathVariable("issueId") Long issueId) {

        Optional<Issue> byId = issueRepository.findById(issueId);

        // 여태까지의 방식 처리
//        if (byId.isEmpty()) {
//            ResponseMessage message = new ResponseMessage(404, "해당 issueId 해당하는 이슈 데이터 없음.");
//            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
//        }

        // 방법1. NullPointException 이 발생하면 에러 메시지 발생하는 방법 : Optional.orElseGet()
        Issue myIssue = byId.orElseGet(() -> throwNullIssue());

//        Issue issue = byId.get();
        ResponseMessage message = new ResponseMessage(200, issueId + " 번 멤버 조회 성공", myIssue);
        return new ResponseEntity(message, HttpStatus.OK);
    }

    public Issue throwNullIssue() {
        return new Issue().builder()
                .id(99L)
                .title("null Issue입니다.")
                .build();
    }

//    @GetMapping("/my/test/issue/{issueId}")
    public ResponseEntity<?> optionalTestTryCatch(@PathVariable("issueId") Long issueId) {

        // 방법 2. try-catch
        Issue findIssue = null;
        try {
//            findIssue = testService.getOneIssueByIssueId(issueId);
        } catch (Exception e) {
            System.out.println("Exception!");
            log.error("logger: NPE?");
            System.out.println("e = " + e);
            ResponseMessage message = new ResponseMessage(404, issueId + " 번 멤버 조회 실패");
            return new ResponseEntity(message, HttpStatus.OK);

        }

        ResponseMessage message = new ResponseMessage(200, issueId + " 번 멤버 조회 성공", findIssue);
        return new ResponseEntity(message, HttpStatus.OK);
    }


    @GetMapping("/my/test/issue/{issueId}")
    public ResponseEntity<?> optionalTestGetElse(@PathVariable("issueId") Long issueId){

//        Optional<Issue> byId = issueRepository.findById(issueId);
//        Issue myIssue = testService.getOneIssueByIssueId(issueId);
        Issue oneIssueByIssueId = testService.getOneIssueByIssueId(issueId);

        // NullPointException 이 발생하면 에러 메시지 발생하는 방법 3: Optional.orElse()
//        Issue myIssue = byId.orElse(null);

//        Issue issue = byId.get();
//        ResponseMessage message = new ResponseMessage(200, issueId + " 번 멤버 조회 성공", myIssue);

        ResponseMessage message = new ResponseMessage(200, issueId + " 번 멤버 조회 성공", "test Data");

        return new ResponseEntity(message, HttpStatus.OK);
    }

//    @GetMapping("/my/test/issue/{projectId}")
//    public ResponseEntity<?> optionalTestGetElse(@PathVariable("projectId") Long projectId) {
//
//        Optional<Issue> byId = issueRepository.findById(projectId);
//
//        testService.getAllIssueByProjectId(projectId);
//        // NullPointException 이 발생하면 에러 메시지 발생하는 방법 3: Optional.orElse()
////        Issue myIssue = byId.orElse(null);
//
////        Issue issue = byId.get();
//        ResponseMessage message = new ResponseMessage(200, issueId + " 번 멤버 조회 성공", myIssue);
//        return new ResponseEntity(message, HttpStatus.OK);
//    }

//    @ExceptionHandler
//    public ResponseEntity<?> exception(Exception e) {
//
//        log.error("Error: " + e);
//        ResponseMessage message = new ResponseMessage(500, "Error");
//        return new ResponseEntity(message, HttpStatus.NOT_IMPLEMENTED);
//    }
}
