package kakao99.backend.project.service;

import jakarta.transaction.Transactional;
import kakao99.backend.common.exception.CustomException;
import kakao99.backend.email.EmailServiceImpl;
import kakao99.backend.entity.Member;
import kakao99.backend.entity.MemberProject;
import kakao99.backend.entity.Project;
import kakao99.backend.entity.types.NotificationType;
import kakao99.backend.group.repository.GroupRepository;
import kakao99.backend.member.repository.MemberRepository;
import kakao99.backend.notification.rabbitmq.dto.RequestMessageDTO;
import kakao99.backend.notification.service.NotificationService;
import kakao99.backend.project.dto.MemberProjectDTO;
import kakao99.backend.project.repository.MemberProjectRepository;
import kakao99.backend.project.repository.ProjectRepository;
import kakao99.backend.common.ResponseMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberProjectService {

    private final ProjectRepository projectRepository;
    private final GroupRepository groupRepository;
    private final MemberProjectRepository memberProjectRepository;
    private final MemberRepository memberRepository;
    private final NotificationService notificationService;
    private final EmailServiceImpl emailService;

    @Transactional
    public ResponseEntity<?> join(MemberProjectDTO memberProjectDTO) throws Exception {

        Optional<Project> optionalProject = projectRepository.findById(memberProjectDTO.getProjectId());
        Project project = optionalProject.get();

        Optional<Member> optionalMember = memberRepository.findById(memberProjectDTO.getMemberId());
        Member member = optionalMember.get();

        MemberProject memberProject = MemberProject.builder()
                .deletedAt(null)
                .project(project)
                .member(member)
                .isActive("true")
                .role("Member")
                .build();

        memberProjectRepository.save(memberProject);

        RequestMessageDTO requestMessageDTO = new RequestMessageDTO().builder()
                .type(NotificationType.NEWMEMBER)
                .specificTypeId(memberProjectDTO.getMemberId())
                .myNickname(member.getNickname())
                .projectId(memberProjectDTO.getProjectId())
                .build();

        notificationService.createNotification(requestMessageDTO);
        emailService.sendSimpleMessage(member.getEmail(), project.getName());

        ResponseMessage message = new ResponseMessage(200, "프로젝트 새로운 멤버 추가 완료");
        return new ResponseEntity<>(message, HttpStatus.OK);
    }
    @Transactional
    public ResponseEntity<?> remove(MemberProjectDTO memberProjectDTO) {
        Optional<MemberProject> optionalMemberProject = memberProjectRepository.findAllByProjectIdAndMemberId(memberProjectDTO.getProjectId(),memberProjectDTO.getMemberId());
        MemberProject memberProject =(MemberProject) optionalMemberProject.get();
        memberProject.deleteMember();

        Member outMember = memberRepository.findById(memberProjectDTO.getMemberId()).get();

        RequestMessageDTO requestMessageDTO = new RequestMessageDTO().builder()
                .type(NotificationType.OUTMEMBER)
                .specificTypeId(memberProjectDTO.getMemberId())
                .myNickname(outMember.getNickname())
                .projectId(memberProjectDTO.getProjectId())
                .build();

        notificationService.createNotification(requestMessageDTO);

        ResponseMessage message = new ResponseMessage(200, "프로젝트에서 멤버 삭제 완료");
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<?> handOverPm(MemberProjectDTO memberProjectDTO, Long memberId) {

        Optional<MemberProject> allByProjectIdAndMemberId = memberProjectRepository.findAllByProjectIdAndMemberId(memberProjectDTO.getProjectId(), memberId);
        if (allByProjectIdAndMemberId.isEmpty()) {
            throw new CustomException(404, "PM이 존재하지 않습니다.");
        }

        memberProjectRepository.updateRole(memberId, memberProjectDTO.getMemberId(), memberProjectDTO.getProjectId());

        ResponseMessage message = new ResponseMessage(200, "PM 권한이 양도 되었습니다.");

        return new ResponseEntity<>(message, HttpStatus.OK);
    }

}
