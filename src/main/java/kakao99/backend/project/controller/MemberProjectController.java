package kakao99.backend.project.controller;

import kakao99.backend.project.dto.MemberProjectDTO;
import kakao99.backend.project.service.MemberProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class MemberProjectController {
    private final MemberProjectService memberProjectService;
    @PostMapping("/api/project/member")
    public ResponseEntity<?> joinProject(@RequestBody MemberProjectDTO memberProjectDTO){

        return memberProjectService.join(memberProjectDTO);
    }

    @DeleteMapping("/api/project/member")
    public ResponseEntity<?> removeProject(@RequestBody MemberProjectDTO memberProjectDTO){

        return memberProjectService.remove(memberProjectDTO);
    }



}
