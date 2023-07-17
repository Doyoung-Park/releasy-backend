package kakao99.backend.issue.controller;

import kakao99.backend.common.exception.ErrorCode;
import kakao99.backend.entity.Issue;
import kakao99.backend.entity.Member;

import kakao99.backend.entity.Project;

import kakao99.backend.common.exception.CustomException;
import kakao99.backend.issue.dto.IssueDTO;


import kakao99.backend.issue.repository.IssueRepository;
import kakao99.backend.issue.repository.IssueRepositoryImpl;
import kakao99.backend.issue.service.IssueService;
import kakao99.backend.member.repository.MemberRepository;
import kakao99.backend.project.repository.ProjectRepository;
import kakao99.backend.common.ResponseMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class IssueController {

    private final IssueRepository issueRepository;
    private final IssueRepositoryImpl issueRepositoryImpl;
    private final IssueService issueService;
    private final MemberRepository memberRepository;
    private final ProjectRepository projectRepository;


    // 이슈 생성
    @PostMapping("/api/{projectId}/issue")
    public ResponseEntity<?> createIssue(@RequestBody IssueForm issue, @PathVariable("projectId") Long projectId) {
        System.out.println("userId = " + issue.getUserId());
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
                .project(project)
                .build();


        issueRepository.save(newIssue);
        ResponseMessage message = new ResponseMessage(200, "이슈 생성 성공");
        return new ResponseEntity(message, HttpStatus.OK);
    }






    @GetMapping("api/{projectId}/issues")
    public ResponseEntity<?> getAllIssues(
            @PathVariable("projectId") Long projectId,
            @RequestParam(value = "status", required = false) String status,
            @RequestParam(value = "type", required = false) String type) {

        ArrayList<IssueDTO> allIssues = null;
        ResponseMessage message = null;

        if (status == null && type == null) {
            allIssues = issueService.getAllIssues(projectId);
            message = new ResponseMessage(200, projectId + "번 프로젝트의 모든 이슈 조회 성공");
        } else if (status != null){
            allIssues = issueService.getAllIssuesByFilter(projectId, status);
            message = new ResponseMessage(200, projectId + "번 프로젝트의 모든 이슈 상태별 조회 성공");

        } else if (type != null){
            allIssues = issueService.getAllIssuesByFilter(projectId, type);
            message = new ResponseMessage(200, projectId + "번 프로젝트의 모든 이슈 타입별 조회 성공");
        }

        message.setData(allIssues);

        return new ResponseEntity(message, HttpStatus.OK);
    }


    // 이슈 정보 업데이트
    @PutMapping("/api/{projectId}/issues/{issueId}")
    public ResponseEntity<?> updateIssue(@PathVariable("projectId") Long projectId, @PathVariable("issueId") Long issueId, @RequestBody UpdateIssueForm updateIssueForm) {
        System.out.println("getStatus = "+ updateIssueForm.getStatus() + updateIssueForm.getIssueType() );
        String result = issueService.updateIssue(updateIssueForm.getTitle(), updateIssueForm.getDescription(),updateIssueForm.getStatus(),updateIssueForm.getIssueType() , issueId);
        ResponseMessage message = null;
        if (result == "OK") {
            message = new ResponseMessage(200, projectId + "번 프로젝트의 모든 이슈 수정 성공");
        } else {
            message = new ResponseMessage(500, projectId + "번 프로젝트의 모든 이슈 수정 실패: " + result);
        }
        return new ResponseEntity(message, HttpStatus.OK);
    }


    // releaseNote id로 모든 이슈 조회
    @GetMapping("/api/releaseNote/{releaseNoteId}/issues")
    public ResponseEntity<?> getIssuesByReleaseNote(@PathVariable("releaseNoteId") Long releaseNoteId) {

        List<IssueDTO> allIssuesByReleaseNoteId = issueService.getAllIssuesByReleaseNoteId(releaseNoteId);
        ResponseMessage message = new ResponseMessage(200, "릴리즈 노트의 관련된 이슈 조회 성공", allIssuesByReleaseNoteId);
        return new ResponseEntity(message, HttpStatus.OK);
    }

    @GetMapping("/api/project/{projectId}/issues")
    public ResponseEntity<?> findAllByNotReleaseNoteId(@PathVariable("projectId") Long projectId) {

        List<IssueDTO> allByNotReleaseNoteId = issueService.findAllByNotReleaseNoteId(projectId);
        ResponseMessage message = new ResponseMessage(200, "릴리즈 노트에 포함되지 않은 이슈 조회 성공", allByNotReleaseNoteId);
        return new ResponseEntity(message, HttpStatus.OK);
    }



    // 예외 처리 예시
    @GetMapping("/test/test/{releaseNoteId}")
    public String exceptionExample(@PathVariable("releaseNoteId") Long releaseNoteId) {

        if(releaseNoteId ==0)
            throw new CustomException(5001, "테스트 Exception");
//            throw new NoSuchElementException("멤ㄴㅇㄹ");

        if (releaseNoteId == 1) {
            throw new CustomException(5001, "테스트 Exception", "IssueController.exceptionExample()");
        }

        if(releaseNoteId == 2) {
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



}


