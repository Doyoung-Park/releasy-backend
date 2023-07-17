package kakao99.backend.memo.controller;

import kakao99.backend.memo.dto.CreateMemoDTO;
import kakao99.backend.memo.dto.MemoCreationResponseDTO;
import kakao99.backend.memo.dto.UpdateMemoDTO;
import kakao99.backend.memo.dto.memoDTO;
import kakao99.backend.memo.service.MemoService;
import kakao99.backend.common.ResponseMessage;
import lombok.RequiredArgsConstructor;
import kakao99.backend.entity.Member;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequiredArgsConstructor
public class MemoController {

    private final MemoService memoService;


    @GetMapping("/api/memo/{projectId}/{issueId}")
    public ResponseEntity<?> getMemo(@PathVariable Long issueId)
    {
        List<memoDTO> memo = memoService.getAllMemo(issueId);

        System.out.println("memo" + memo);

        ResponseMessage message = new ResponseMessage(200, issueId+"번 이슈의 모든 댓글 조회 성공");
        message.setData(memo);

        return new ResponseEntity(message, HttpStatus.OK);
    }

    @PostMapping("/api/memo/{projectId}/{issueId}")
    public ResponseEntity<?> createMemo(@PathVariable Long issueId , @RequestBody CreateMemoDTO obj, Authentication authentication)
    {
        Member member = (Member) authentication.getPrincipal();
        System.out.println(member.getId() + obj.getIssueId() + obj.getContent() + obj.getCreatedAt());
        MemoCreationResponseDTO newMemo= memoService.createMemo(member.getId(),obj.getIssueId(),obj.getContent(),obj.getCreatedAt());
        return new ResponseEntity<>(newMemo , HttpStatus.OK);

    }

    @PatchMapping("/api/memo/{projectId}/{issueId}/{memoId}")
    public ResponseEntity<?> patchMemo(@PathVariable Long memoId,@RequestBody UpdateMemoDTO obj,Authentication authentication)
    {
        Member member = (Member) authentication.getPrincipal();
        ResponseEntity<?> updatedMemoId = memoService.updateMemo(memoId,obj);
        ResponseMessage message = new ResponseMessage(200, memoId+"번 댓글 수정 성공");
        message.setData(updatedMemoId);

        return new ResponseEntity(updatedMemoId, HttpStatus.OK);
    }


    @DeleteMapping("/api/memo/{projectId}/{issueId}/{memoId}")
    public ResponseEntity<?> deleteMemo(@PathVariable Long memoId)
    {
        ResponseEntity<?> updatedMemoId = memoService.deleteMemo(memoId);

        ResponseMessage message = new ResponseMessage(200, memoId+"번 댓글 삭제 성공");
        message.setData(updatedMemoId);

        return new ResponseEntity(message, HttpStatus.OK);

    }


}
