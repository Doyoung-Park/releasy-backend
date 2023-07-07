package kakao99.backend.issue.cotroller;

import kakao99.backend.entity.Issue;
import kakao99.backend.entity.Member;
import kakao99.backend.issue.repository.IssueRepository;
import kakao99.backend.member.repository.MemberRepository;
import kakao99.backend.utils.ResponseMessage;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping( "/issue")
@RequiredArgsConstructor
public class IssueController {

    private final ResponseMessage responseMessage;
    private final IssueRepository issueRepository;
    private final MemberRepository memberRepository;

    @PostMapping("/new")
    public ResponseEntity<?> createIssue(IssueForm issue, Long userId) {
        System.out.println("userId = " + userId);
        Optional<Member> byId = memberRepository.findById(userId);

        if (byId.isEmpty()) {
            ResponseMessage message = responseMessage.createMessage(404, "해당 userId에 해당하는 유저 데이터 없음.");
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        }
        Member member = byId.get();
        Issue newIssue = new Issue().builder()
                .title(issue.getTitle())
                .issueType(issue.getType())
                .description(issue.getDescription())
                .memberReport(member)
                .build();

        issueRepository.save(newIssue);
        ResponseMessage message = responseMessage.createMessage(200, "이슈 생성 성공");

        return new ResponseEntity(message, HttpStatus.OK);
    }

}
