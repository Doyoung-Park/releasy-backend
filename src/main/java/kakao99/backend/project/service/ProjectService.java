package kakao99.backend.project.service;

import jakarta.transaction.Transactional;
import kakao99.backend.entity.Group;
import kakao99.backend.entity.Member;
import kakao99.backend.entity.MemberProject;
import kakao99.backend.entity.Project;
import kakao99.backend.group.repository.GroupRepository;
import kakao99.backend.project.dto.ProjectDTO;
import kakao99.backend.project.dto.ProjectIdDTO;
import kakao99.backend.project.dto.ProjectModifyDTO;
import kakao99.backend.project.dto.ProjectPMDTO;
import kakao99.backend.project.repository.MemberProjectRepository;
import kakao99.backend.project.repository.ProjectRepository;
import kakao99.backend.common.ResponseMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

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

            return new ResponseEntity<>(message, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        ResponseMessage message = new ResponseMessage(200, "프로젝트 수정 성공");
        return new ResponseEntity<>(message, HttpStatus.OK);
    }
    @Transactional
    public ResponseEntity<?> removeProject(ProjectModifyDTO projectModifyDTO, Long memberId){
        Optional<Project> optionalProject = projectRepository.findById(projectModifyDTO.getId());
        System.out.println(projectModifyDTO.getId());
        if(optionalProject.isEmpty()) {
            ResponseMessage message = new ResponseMessage(500, "id로 삭제할 프로젝트 확인 실패");

            return new ResponseEntity<>(message, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        Project project = optionalProject.get();
        Optional<String> opRole = memberProjectRepository.role(project.getId(), memberId);
        if(opRole.isEmpty()){
            ResponseMessage message = new ResponseMessage(500, "id로 찾은 role 확인 실패");

            return new ResponseEntity<>(message, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        String role = opRole.get();
        System.out.println(role);
        if(role.equals("PM")) {
            memberProjectRepository.deleteMemberProjectByProjectId(projectModifyDTO.getId());
            project.deleteProject();
            ResponseMessage message = new ResponseMessage(200, "프로젝트 삭제 성공");
            return new ResponseEntity<>(message, HttpStatus.OK);
        }else{
            ResponseMessage message = new ResponseMessage(200, "프로젝트 삭제 권한이 없습니다.");
            return new ResponseEntity<>(message, HttpStatus.OK);
        }
    }

    @Transactional
    public ResponseEntity<?> getProject(Long projectId, Long memberId){
        Optional<Project> optionalProject = projectRepository.findById(projectId);
        if(optionalProject.isEmpty()) {
                ResponseMessage message = new ResponseMessage(500, "id로 삭제할 프로젝트 확인 실패");
                return new ResponseEntity<>(message, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        Optional<String> opRole = memberProjectRepository.role(projectId, memberId);
        if(opRole.isEmpty()){
            ResponseMessage message = new ResponseMessage(500, "role id 찾을 수 없음");
            return new ResponseEntity<>(message, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        String role = opRole.get();
        Project project = optionalProject.get();
        ProjectIdDTO projectIdDTO = ProjectIdDTO.builder()
                .id(project.getId())
                .groupName(project.getGroup().getName())
                .groupCode(project.getGroup().getCode())
                .name(project.getName())
                .role(role)
                .status(project.getStatus())
                .description(project.getDescription())
                .createAt(project.getCreatedAt())
                .build();
        System.out.println(projectIdDTO.getRole());
        ResponseMessage message = new ResponseMessage(200, "프로젝트 정보 조회 성공", projectIdDTO);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    public String findRole(long projectId, long memberId) throws Exception {
        Optional<String> opRole = memberProjectRepository.role(projectId, memberId);
        if(opRole.isEmpty()){
            throw new Exception("role id 찾을 수 없음");
        }
        return opRole.get();
    }

    @Transactional
    public List<ProjectPMDTO> makeProjectPMDTOS(List<Project> projects) throws Exception {
        List<ProjectPMDTO> projectPMDTOS = new ArrayList<>();
        for (Project project : projects) {
            System.out.println(project.getId());
            Optional<Member> optionalMember = memberProjectRepository.findPMByProjectId(project.getId(), "PM");
            if(optionalMember.isEmpty()){
                throw new Exception("PM이 없음");
            }
            Member pmMember = optionalMember.get();
            ProjectPMDTO projectPMDTO = ProjectPMDTO.builder()
                    .PMId(pmMember.getId())
                    .PMName(pmMember.getNickname())
                    .id(project.getId())
                    .name(project.getName())
                    .status(project.getStatus())
                    .description(project.getDescription())
                    .createdAt(project.getCreatedAt())
                    .groupId(project.getGroup().getId())
                    .isActive(project.getIsActive())
                    .updatedAt(project.getUpdatedAt())
                    .build();
            projectPMDTOS.add(projectPMDTO);
        }

        return projectPMDTOS;
    }

}
