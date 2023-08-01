package kakao99.backend.project.controller;

import kakao99.backend.entity.Member;
import kakao99.backend.project.dto.MemberProjectDTO;
import kakao99.backend.project.service.MemberProjectService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
public class MemberProjectController {
    private final MemberProjectService memberProjectService;
    @PostMapping("/api/project/member")
    public ResponseEntity<?> joinProject(@RequestBody MemberProjectDTO memberProjectDTO){

        return memberProjectService.join(memberProjectDTO);
    }

    @DeleteMapping("/api/project/member")
    public ResponseEntity<?> removeProject(@RequestBody MemberProjectDTO memberProjectDTO, Authentication authentication){

        log.info("authentication={}", authentication);

        return memberProjectService.remove(memberProjectDTO);
    }

    //PM 권한 양도
    @PatchMapping("/api/project/role/pm")
    public ResponseEntity<?> assignPm(@RequestBody MemberProjectDTO memberProjectDTO, Authentication authentication) {

        log.info("authentication={}", authentication);
        log.info("projectId={}", memberProjectDTO.getProjectId());

        Member member = (Member) authentication.getPrincipal();

        return memberProjectService.handOverPm(memberProjectDTO, member.getId());

    }
}
