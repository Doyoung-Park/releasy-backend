package kakao99.backend.project.controller;

import kakao99.backend.entity.Member;
import kakao99.backend.entity.Project;
import kakao99.backend.project.dto.ProjectDTO;
import kakao99.backend.project.dto.ProjectModifyDTO;
import kakao99.backend.project.dto.ProjectPMDTO;
import kakao99.backend.project.repository.MemberProjectRepository;
import kakao99.backend.project.repository.ProjectRepository;
import kakao99.backend.project.service.ProjectService;
import kakao99.backend.common.ResponseMessage;
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
    public ResponseEntity<?> createProject(Authentication authentication, @RequestBody ProjectDTO projectDTO){
        Member member = (Member) authentication.getPrincipal();
        return projectService.saveProject(projectDTO, member);
    }


    @PutMapping("/api/project")
    public ResponseEntity<?> patchProject(@RequestBody ProjectModifyDTO projectModifyDTO){
        System.out.println(projectModifyDTO.getId());
        System.out.println(projectModifyDTO.getName());
        System.out.println(projectModifyDTO.getStatus());
        System.out.println(projectModifyDTO.getDescription());

        return projectService.updateProject(projectModifyDTO);
    }

    @DeleteMapping("/api/project")
    public ResponseEntity<?> removeProject(@RequestBody ProjectModifyDTO projectModifyDTO,Authentication authentication){
        Member member = (Member) authentication.getPrincipal();
        return projectService.removeProject(projectModifyDTO, member.getId());
    }

    //프로젝트 정보 조회
    @GetMapping("/api/project/{projectId}")
    public ResponseEntity<?> getProjectInfo(@PathVariable("projectId") Long projectId, Authentication authentication) {
        Member member = (Member) authentication.getPrincipal();

        return projectService.getProject(projectId, member.getId());
    }

    //현재 프로젝트 권한 조회
    @GetMapping("/api/role/{projectId}")
    public ResponseEntity<?> getRole(@PathVariable("projectId") Long projectId, Authentication authentication) throws Exception {
        Member member = (Member) authentication.getPrincipal();
        try {
            String role =projectService.findRole(projectId, member.getId());
            ResponseMessage message = new ResponseMessage(200, "프로젝트 권한 조회 완료", role);
            return new ResponseEntity(message, HttpStatus.OK);
        }catch (Exception e){
            ResponseMessage message = new ResponseMessage(404, "해당 id로 찾은 role 데이터 없음.");
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        }
    }

    //내가 속한 프로젝트 조회
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
    @GetMapping("/api/otherProject")
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
    //선택한 프로젝트 조회



}


