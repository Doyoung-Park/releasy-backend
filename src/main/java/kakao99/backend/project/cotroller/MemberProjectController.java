package kakao99.backend.project.cotroller;

import kakao99.backend.entity.Member;
import kakao99.backend.entity.Project;
import kakao99.backend.project.dto.MemberProjectDTO;
import kakao99.backend.project.dto.ProjectDTO;
import kakao99.backend.project.service.MemberProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class MemberProjectController {
    private final MemberProjectService memberProjectService;
    @PostMapping("/project/member")
    public ResponseEntity<?> joinProject(@RequestBody MemberProjectDTO memberProjectDTO){

        return memberProjectService.join(memberProjectDTO);
    }

    @DeleteMapping("/project/member")
    public ResponseEntity<?> removeProject(@RequestBody MemberProjectDTO memberProjectDTO){

        return memberProjectService.remove(memberProjectDTO);
    }

}
