package kakao99.backend.project.cotroller;

import com.nimbusds.oauth2.sdk.Response;
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
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static io.lettuce.core.pubsub.PubSubOutput.Type.message;

@RestController
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;
    private final ProjectRepository projectRepository;
    private final ResponseMessage responseMessage;

    @PostMapping("/project")
    public ResponseEntity<?> createProject(@RequestBody ProjectDTO projectDTO, Authentication authentication){
        Member member = (Member) authentication.getPrincipal();
        return projectService.saveProject(projectDTO, member);
    }


    @PatchMapping("/project")
    public ResponseEntity<?> patchProject(@RequestBody ProjectModifyDTO projectModifyDTO){
        return projectService.updateProject(projectModifyDTO);
    }

    @DeleteMapping("/project")
    public ResponseEntity<?> removeProject(@RequestBody ProjectModifyDTO projectModifyDTO){
        return projectService.removeProject(projectModifyDTO);
    }

    @GetMapping("/project")
    public ResponseEntity<?> getProject(Authentication authentication){
        Member member = (Member) authentication.getPrincipal();
        System.out.println(member.getGroup().getCode());
        List<Project> project = projectRepository.findAllByGroupIdAndIsActive(member.getGroup().getId(), "true");

        ResponseMessage message = responseMessage.createMessage(200, "내 프로젝트 목록 조회 완료", project);

        return new ResponseEntity<>(message, HttpStatus.OK);
    }

}
