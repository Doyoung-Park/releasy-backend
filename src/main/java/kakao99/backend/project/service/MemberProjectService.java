package kakao99.backend.project.service;

import jakarta.transaction.Transactional;
import kakao99.backend.entity.Group;
import kakao99.backend.entity.Member;
import kakao99.backend.entity.MemberProject;
import kakao99.backend.entity.Project;
import kakao99.backend.group.repository.GroupRepository;
import kakao99.backend.member.repository.MemberRepository;
import kakao99.backend.project.dto.MemberProjectDTO;
import kakao99.backend.project.dto.ProjectDTO;
import kakao99.backend.project.repository.MemberProjectRepository;
import kakao99.backend.project.repository.ProjectRepository;
import kakao99.backend.utils.ResponseMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberProjectService {

    private final ProjectRepository projectRepository;
    private final GroupRepository groupRepository;
    private final MemberProjectRepository memberProjectRepository;
    private final MemberRepository memberRepository;

    public ResponseEntity<?> join(MemberProjectDTO memberProjectDTO) {

        Optional<Project> optionalProject = projectRepository.findById(memberProjectDTO.getProjectId());
        Project project = optionalProject.get();

        Optional<Member> optionalMember = memberRepository.findById(memberProjectDTO.getMemberId());
        Member member = optionalMember.get();

        MemberProject memberProject = MemberProject.builder()
                .deletedAt(null)
                .project(project)
                .member(member)
                .isActive("true")
                .role("slave")
                .build();

        memberProjectRepository.save(memberProject);
        ResponseMessage message = new ResponseMessage(200, "프로젝트 새로운 멤버 추가 완료");
        return new ResponseEntity<>(message, HttpStatus.OK);
    }
    @Transactional
    public ResponseEntity<?> remove(MemberProjectDTO memberProjectDTO) {
        Optional<MemberProject> optionalMemberProject = memberProjectRepository.findAllByProjectIdAndMemberId(memberProjectDTO.getProjectId(),memberProjectDTO.getMemberId());
        MemberProject memberProject =(MemberProject) optionalMemberProject.get();
        memberProject.deleteMember();
        ResponseMessage message = new ResponseMessage(200, "프로젝트에서 멤버 삭제 완료");
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

}
