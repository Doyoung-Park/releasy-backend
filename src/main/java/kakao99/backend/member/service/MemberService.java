package kakao99.backend.member.service;

import kakao99.backend.entity.Group;
import kakao99.backend.entity.Issue;
import kakao99.backend.entity.Member;
import kakao99.backend.entity.Project;
import kakao99.backend.group.repository.GroupRepository;
import kakao99.backend.jwt.TokenProvider;
import kakao99.backend.member.dto.LoginDTO;
import kakao99.backend.member.dto.MemberInfoDTO;
import kakao99.backend.member.dto.RegisterDTO;
import kakao99.backend.member.repository.MemberRepository;
import kakao99.backend.project.repository.MemberProjectRepository;
import kakao99.backend.utils.ResponseMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;
    private final ResponseMessage responseMessage;
    private final GroupRepository groupRepository;
    private final MemberProjectRepository memberProjectRepository;

    @Transactional
    public ResponseEntity<?> join(RegisterDTO registerDTO) {

        Optional<Member> findEmailMember = memberRepository.findByEmail(registerDTO.getEmail());

        if (findEmailMember.isPresent()) {
            ResponseMessage message = responseMessage.createMessage(400, "이미 가입된 이메일 입니다.");
            return new ResponseEntity<>(message, HttpStatus.OK);
        }

        Group group = Group.builder()
                .name(registerDTO.getGroupName())
                .createdAt(new Date())
                .isActive("true")
                .code(UUID.randomUUID().toString())
                .build();

        Member member = Member.builder()
                .username(registerDTO.getName())
                .nickname(registerDTO.getNickname())
                .email(registerDTO.getEmail())
                .password(passwordEncoder.encode(registerDTO.getPassword()))
                .createdAt(new Date())
                .position(registerDTO.getPosition())
                .group(group)
                .isActive(false)
                .build();

        memberRepository.save(member);
        ResponseMessage message = responseMessage.createMessage(200,"회원가입이 완료 되었습니다.");
        return new ResponseEntity<>(message,HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<?> create(RegisterDTO registerDTO) {
        Optional<Group> byCode = groupRepository.findByCode(registerDTO.getGroupName());

        if (byCode.isEmpty()) {
            ResponseMessage message = responseMessage.createMessage(404, "그룹이 존재하지 않습니다.");
            return new ResponseEntity<>(message,HttpStatus.OK);
        }

        Group group = byCode.get();

        Member member = Member.builder()
                .username(registerDTO.getName())
                .nickname(registerDTO.getNickname())
                .email(registerDTO.getEmail())
                .password(passwordEncoder.encode(registerDTO.getPassword()))
                .createdAt(new Date())
                .position(registerDTO.getPosition())
                .group(group)
                .isActive(true)
                .build();

        memberRepository.save(member);

        ResponseMessage message = responseMessage.createMessage(200,"회원가입이 완료 되었습니다.");

        return new ResponseEntity<>(message,HttpStatus.OK);
    }

    @Transactional(readOnly = true)
    public ResponseEntity<?> login(LoginDTO loginDTO) {

        Optional<Member> byEmail = memberRepository.findByEmail(loginDTO.getEmail());

        if (byEmail.isEmpty()) {
            ResponseMessage message = responseMessage.createMessage(404, "존재하지 않는 이메일 입니다.");
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        }

        Member member = byEmail.get();

        if (!checkPassword(loginDTO.getPassword(), member.getPassword())) {
            ResponseMessage message = responseMessage.createMessage(400, "비밀번호가 일치 하지 않습니다.");
            return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
        }

        String accessToken = tokenProvider.createAccessToken(member);
        ResponseMessage message = responseMessage.createMessage(200, "로그인이 완료 되었습니다.", accessToken);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<?> getMemberInfo(Long memberId) {
        Optional<Member> byId = memberRepository.findById(memberId);

        if (byId.isEmpty()) {
            ResponseMessage message = responseMessage.createMessage(404, "회원 정보가 존재하지 않습니다.");
            return new ResponseEntity<>(message,HttpStatus.OK);
        }

        Member member = byId.get();

        List<Project> projectList = memberProjectRepository.findProjectByMemberId(memberId);
        MemberInfoDTO memberInfoDTO = MemberInfoDTO.builder()
                .name(member.getUsername())
                .nickname(member.getNickname())
                .email(member.getEmail())
                .groupName(member.getGroup().getName())
                .position(member.getPosition())
                .projectList(projectList)
                .build();

        ResponseMessage message = responseMessage.createMessage(200, "회원 정보 조회 완료", memberInfoDTO);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    Boolean checkPassword(String rawPassword, String encodePassword) {

        return passwordEncoder.matches(rawPassword, encodePassword);
    }

    public ResponseEntity<?> getMemberOfProject(Long projectId) {
        List<Member> memberByProjectId = memberProjectRepository.findMemberByProjectId(projectId);
        if (memberByProjectId.isEmpty()) {
            ResponseMessage message = responseMessage.createMessage(404, projectId+"번 프로젝트에 해당하는 회원이 존재하지 않습니다.");
            return new ResponseEntity<>(message,HttpStatus.OK);
        }

        List<MemberInfoDTO> memberInfoDTOList = new ArrayList<>();

            for (Member member : memberByProjectId) {
                MemberInfoDTO memberInfoDTO = MemberInfoDTO.builder()
                        .name(member.getUsername())
                        .nickname(member.getNickname())
                        .email(member.getEmail())
//                        .groupName(member.getGroup().getName())
                        .position(member.getPosition())
//                        .projectList(projectList)
                        .build();
                memberInfoDTOList.add(memberInfoDTO);
            }
        ResponseMessage message = responseMessage.createMessage(200, projectId+"번 프로젝트의 회원 정보 조회 완료", memberInfoDTOList);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }
}
