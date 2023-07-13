package kakao99.backend.project.controller;

import com.nimbusds.oauth2.sdk.Response;
import kakao99.backend.entity.Member;
import kakao99.backend.entity.MemberProject;
import kakao99.backend.entity.Project;
import kakao99.backend.project.dto.ProjectDTO;
import kakao99.backend.project.dto.ProjectModifyDTO;
import kakao99.backend.project.dto.ProjectPMDTO;
import kakao99.backend.project.repository.MemberProjectRepository;
import kakao99.backend.project.repository.ProjectRepository;
import kakao99.backend.project.service.ProjectService;
import kakao99.backend.utils.ResponseMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static io.lettuce.core.pubsub.PubSubOutput.Type.message;

@RestController
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;
    private final ProjectRepository projectRepository;
    private final MemberProjectRepository memberProjectRepository;


    @PostMapping("/api/project")
    public ResponseEntity<?> createProject(Authentication authentication, @RequestBody ProjectDTO projectDTO){
        Member member = (Member) authentication.getPrincipal();
        return projectService.saveProject(projectDTO, member);
    }


    @PutMapping("/api/project")
    public ResponseEntity<?> patchProject(@RequestBody ProjectModifyDTO projectModifyDTO){
        return projectService.updateProject(projectModifyDTO);
    }

    @DeleteMapping("/api/project")
    public ResponseEntity<?> removeProject(@RequestBody ProjectModifyDTO projectModifyDTO,Authentication authentication){
        Member member = (Member) authentication.getPrincipal();
        return projectService.removeProject(projectModifyDTO, member.getId());
    }


    @GetMapping("/api/myProject")
    public ResponseEntity<?> getMyProject(Authentication authentication){
        Member member = (Member) authentication.getPrincipal();
        List<Project> projects = memberProjectRepository.findProjectByMemberId(member.getId(), "true");

        List<ProjectPMDTO> projectPMDTOS = null;
        try {
            projectPMDTOS = projectService.makeProjectPMDTOS(projects);
        } catch (Exception e) {
            ResponseMessage message = new ResponseMessage(404, "해당 PM 유저 데이터 없음.");

            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        }

        ResponseMessage message = new ResponseMessage(200, "내가 속한 프로젝트 목록 조회 완료", projectPMDTOS);

        return new ResponseEntity(message, HttpStatus.OK);
    }


    //  내가 속한 그룹에서 내가 포함되지 않은 프로젝트 조회해오기
    @GetMapping("api/otherProject")
    public ResponseEntity<?> getProjectFromGroup(Authentication authentication) {
        Member member = (Member) authentication.getPrincipal();
        System.out.println("userId = " + member.getId());
        List<Project> othersProject = memberProjectRepository.findOtherProject(member.getId(), member.getGroup().getId(), "true");
        List<ProjectPMDTO> projectPMDTOS = null;
        try {
            projectPMDTOS = projectService.makeProjectPMDTOS(othersProject);
        } catch (Exception e) {
            ResponseMessage message = new ResponseMessage(404, "해당 PM 유저 데이터 없음.");

            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        }

        ResponseMessage message = new ResponseMessage(200, "내가 속하지 않은 프로젝트 목록 조회 완료", projectPMDTOS);

        return new ResponseEntity(message, HttpStatus.OK);
    }

}

