package kakao99.backend.project.controller;

import kakao99.backend.entity.Member;
import kakao99.backend.entity.Project;
import kakao99.backend.project.dto.ProjectDTO;
import kakao99.backend.project.dto.ProjectModifyDTO;
import kakao99.backend.project.repository.MemberProjectRepository;
import kakao99.backend.project.repository.ProjectRepository;
import kakao99.backend.project.service.ProjectService;
import kakao99.backend.utils.ResponseMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;
    private final ProjectRepository projectRepository;
    private final MemberProjectRepository memberProjectRepository;


    @PostMapping("/api/project")
    public ResponseEntity<?> createProject(@RequestBody ProjectDTO projectDTO, Authentication authentication){
        Member member = (Member) authentication.getPrincipal();
        return projectService.saveProject(projectDTO, member);
    }


    @PutMapping("/api/project")
    public ResponseEntity<?> patchProject(@RequestBody ProjectModifyDTO projectModifyDTO){
        return projectService.updateProject(projectModifyDTO);
    }

    @DeleteMapping("/api/project")
    public ResponseEntity<?> removeProject(@RequestBody ProjectModifyDTO projectModifyDTO){
        return projectService.removeProject(projectModifyDTO);
    }


    @GetMapping("/api/myProject")
    public ResponseEntity<?> getMyProject(Authentication authentication){
        Member member = (Member) authentication.getPrincipal();

        List<Project> project = memberProjectRepository.findProjectByMemberId(member.getId(), "true");

        ResponseMessage message = new ResponseMessage(200, "내가 속한 프로젝트 목록 조회 완료", project);

        return new ResponseEntity(message, HttpStatus.OK);
    }


    //  내가 속한 그룹에서 내가 포함되지 않은 프로젝트 조회해오기
    @GetMapping("api/otherProject")
    public ResponseEntity<?> getProjectFromGroup(Authentication authentication) {
        Member member = (Member) authentication.getPrincipal();
        System.out.println("userId = " + member.getId());

        List<Project> othersProject = memberProjectRepository.findOtherProject(member.getId(), member.getGroup().getId(), "true");

        ResponseMessage message = new ResponseMessage(200, "내가 속하지 않은 프로젝트 목록 조회 완료", othersProject);

        return new ResponseEntity(message, HttpStatus.OK);
    }

}


