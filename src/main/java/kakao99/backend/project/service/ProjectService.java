package kakao99.backend.project.service;

import jakarta.transaction.Transactional;
import kakao99.backend.entity.Group;
import kakao99.backend.entity.Member;
import kakao99.backend.entity.MemberProject;
import kakao99.backend.entity.Project;
import kakao99.backend.group.dto.GroupNameDTO;
import kakao99.backend.group.repository.GroupRepository;
import kakao99.backend.project.dto.ProjectDTO;
import kakao99.backend.project.dto.ProjectModifyDTO;
import kakao99.backend.project.repository.MemberProjectRepository;
import kakao99.backend.project.repository.ProjectRepository;
import kakao99.backend.utils.ResponseMessage;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final GroupRepository groupRepository;
    private final MemberProjectRepository memberProjectRepository;

    @Transactional
    public ResponseEntity<?> saveProject(ProjectDTO projectDTO, Member member){
        Optional<Group> optionalGroup = groupRepository.findById(member.getGroup().getId());
        Group group = optionalGroup.get();

        Project project =Project.builder()
                .name(projectDTO.getName())
                .description(projectDTO.getDescription())
                .status(projectDTO.getStatus())
                .isActive("true")
                .deletedAt(null)
                .group(group)
                .build();


        MemberProject memberProject = MemberProject.builder()
                .deletedAt(null)
                .project(project)
                .member(member)
                .isActive("true")
                .role("PM")
                .build();

        projectRepository.save(project);
        memberProjectRepository.save(memberProject);
        ResponseMessage message = new ResponseMessage(200, "프로젝트 생성 성공");
        return new ResponseEntity(message, HttpStatus.OK);
    }


    @Transactional
    public ResponseEntity<?> updateProject(ProjectModifyDTO projectModifyDTO){
        try {
            Optional<Project> optionalProject = projectRepository.findById(projectModifyDTO.getId());
            Project project = optionalProject.get();
            project.updateProject(projectModifyDTO);
        }
        catch(Exception e){
            ResponseMessage message = new ResponseMessage(200, "프로젝트 수정 실패");

            return new ResponseEntity(message, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        ResponseMessage message = new ResponseMessage(200, "프로젝트 수정 성공");
        return new ResponseEntity(message, HttpStatus.OK);
    }
    @Transactional
    public ResponseEntity<?> removeProject(ProjectModifyDTO projectModifyDTO){
        try {
            Optional<Project> optionalProject = projectRepository.findById(projectModifyDTO.getId());
            Project project = optionalProject.get();
            memberProjectRepository.deleteMemberProjectByProjectId(projectModifyDTO.getId());
            project.deleteProject();
        }
        catch(Exception e){
            ResponseMessage message = new ResponseMessage(500, "프로젝트 삭제 실패");

            return new ResponseEntity(message, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        ResponseMessage message = new ResponseMessage(200, "프로젝트 삭제 성공");
        return new ResponseEntity(message, HttpStatus.OK);
    }
}
