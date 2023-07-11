package kakao99.backend.memo.cotroller;

import kakao99.backend.memo.dto.CreateMemoDTO;
import kakao99.backend.memo.dto.UpdateMemoDTO;
import kakao99.backend.memo.dto.memoDTO;
import kakao99.backend.memo.repository.MemoRepository;
import kakao99.backend.memo.service.MemoService;
import kakao99.backend.utils.ResponseMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class MemoController {

    private final MemoService memoService;

    private final ResponseMessage responseMessage;



    @GetMapping("/api/memo/{projectId}/{issueId}")
    public ResponseEntity<?> getMemo(@PathVariable Long issueId)
    {
        List<memoDTO> memo = memoService.getAllMemo(issueId);
        System.out.println("memo" + memo);

        ResponseMessage message = responseMessage.createMessage(200, issueId+"번 이슈의 모든 댓글 조회 성공");
        message.setData(memo);

        return new ResponseEntity(message, HttpStatus.OK);
    }

    @PostMapping("/api/memo/{projectId}/{issueId}/new")
    public ResponseEntity<?> createMemo(@PathVariable Long issueId ,@RequestBody CreateMemoDTO obj)
    {
        memoService.createMemo(obj.getMemberId(),obj.getIssueId(),obj.getContent(),obj.getCreatedAt());
        return new ResponseEntity<>("댓글 생성" , HttpStatus.OK);

    }

    @PatchMapping("/api/memo/{projectId}/{issueId}/{memoId}/patch")
    public ResponseEntity<?> patchMemo(@PathVariable Long memoId,@RequestBody UpdateMemoDTO obj)
    {
        ResponseEntity<?> updatedMemoId = memoService.updateMemo(memoId,obj);
        ResponseMessage message = responseMessage.createMessage(200, memoId+"번 댓글 수정 성공");
        message.setData(updatedMemoId);

        return new ResponseEntity(updatedMemoId, HttpStatus.OK);
    }


    @DeleteMapping("/api/memo/{projectId}/{issueId}/{memoId}/delete")
    public ResponseEntity<?> deleteMemo(@PathVariable Long memoId)
    {
        ResponseEntity<?> updatedMemoId = memoService.deleteMemo(memoId);

        ResponseMessage message = responseMessage.createMessage(200, memoId+"번 댓글 삭제 성공");
        message.setData(updatedMemoId);

        return new ResponseEntity(updatedMemoId, HttpStatus.OK);

    }


}
