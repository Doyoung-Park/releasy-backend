package kakao99.backend.member.cotroller;

import kakao99.backend.entity.Member;
import kakao99.backend.member.dto.LoginDTO;
import kakao99.backend.member.dto.RegisterDTO;
import kakao99.backend.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/api/member/signup/group")
    public ResponseEntity<?> groupCtreate(@Validated @RequestBody RegisterDTO registerDTO) {
        log.info("회원 가입 컨트롤러 시작");
        return memberService.join(registerDTO);

    }

    @PostMapping("/api/member/signup/group/join")
    public ResponseEntity<?> groupJoin(@Validated @RequestBody RegisterDTO registerDTO) {
        log.info("회원 가입 컨트롤러 시작");
        return memberService.create(registerDTO);

    }

    @PostMapping("/api/member/login")
    public ResponseEntity<?> login(@Validated @RequestBody LoginDTO loginDTO) {

        log.info("email={}", loginDTO.getEmail());
        log.info("password={}", loginDTO.getPassword());
        return memberService.login(loginDTO);
    }

    @GetMapping("/api/member")
    public ResponseEntity<?> memberInfo(Authentication authentication) {
        Member member = (Member) authentication.getPrincipal();

        return memberService.getMemberInfo(member.getId());
    }
}
