package kakao99.backend.member.service;

import kakao99.backend.common.exception.CustomException;
import kakao99.backend.entity.Group;
import kakao99.backend.entity.Member;
import kakao99.backend.entity.MemberProject;
import kakao99.backend.entity.Project;
import kakao99.backend.group.repository.GroupRepository;
import kakao99.backend.jwt.TokenProvider;
import kakao99.backend.member.dto.*;
import kakao99.backend.member.repository.MemberRepository;
import kakao99.backend.project.repository.MemberProjectRepository;
import kakao99.backend.common.ResponseMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;
    private final GroupRepository groupRepository;
    private final MemberProjectRepository memberProjectRepository;

    @Transactional
    public Long create(RegisterDTO registerDTO) {

        Optional<Member> findEmailMember = memberRepository.findByEmail(registerDTO.getEmail());

        if (findEmailMember.isPresent()) {
            throw new CustomException(404,"이미 가입된 이메일이 존재 합니다.");
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
                .authority("GM")
                .email(registerDTO.getEmail())
                .provider(registerDTO.getProvider())
                .password(passwordEncoder.encode(registerDTO.getPassword()))
                .createdAt(new Date())
                .position(registerDTO.getPosition())
                .group(group)
                .isActive(true)
                .build();

        Member savedMember = memberRepository.save(member);
        return savedMember.getId();
    }

    //그룹에서 삭제당하고 다시 그룹을 만들 때
    @Transactional
    public void createRejoin(ReJoinDTO reJoinDTO) {
        Optional<Member> findEmailMember = memberRepository.findByEmail(reJoinDTO.getEmail());
        if (findEmailMember.isEmpty()) {
            throw new CustomException(404, "멤버가 존재하지 않습니다.");
        }
        Member member = findEmailMember.get();
        Group group = Group.builder()
                .name(reJoinDTO.getGroupName())
                .createdAt(new Date())
                .isActive("true")
                .code(UUID.randomUUID().toString())
                .build();

        member.updateGroup(group, "GM");
    }

    @Transactional
    public Long join(RegisterDTO registerDTO) {
        Optional<Group> byCode = groupRepository.findByCode(registerDTO.getGroupName());

        if (byCode.isEmpty()) {
            throw new CustomException(404, "그룹이 존재하지 않습니다.");
        }

        Group group = byCode.get();

        Member member = Member.builder()
                .username(registerDTO.getName())
                .nickname(registerDTO.getNickname())
                .authority("Slave")
                .provider(registerDTO.getProvider())
                .email(registerDTO.getEmail())
                .password(passwordEncoder.encode(registerDTO.getPassword()))
                .createdAt(new Date())
                .position(registerDTO.getPosition())
                .group(group)
                .isActive(true)
                .build();

        Member savedMember = memberRepository.save(member);
        return savedMember.getId();
    }


    @Transactional
    public void rejoin(ReJoinDTO reJoinDTD) {
        Optional<Group> byCode = groupRepository.findByCode(reJoinDTD.getGroupName());
        if (byCode.isEmpty()) {
            throw new CustomException(404, "그룹이 존재하지 않습니다.");
        }
        Optional<Member> findEmailMember = memberRepository.findByEmail(reJoinDTD.getEmail());
        if (findEmailMember.isEmpty()) {
            throw new CustomException(404, "멤버가 존재하지 않습니다.");
        }
        Group group = byCode.get();
        Member member = findEmailMember.get();

        member.updateGroup(group, "Slave");
    }

    @Transactional(readOnly = true)
    public String login(LoginDTO loginDTO) {

        Optional<Member> byEmail = memberRepository.findByEmail(loginDTO.getEmail());

        if (byEmail.isEmpty()) {
            //ResponseMessage message = new ResponseMessage(404, "존재하지 않는 이메일 입니다.");
            throw new CustomException(404, "존재하지 않는 이메일입니다.");
        }

        Member member = byEmail.get();

        if (!checkPassword(loginDTO.getPassword(), member.getPassword())) {
            //ResponseMessage message = new ResponseMessage(400, "비밀번호가 일치 하지 않습니다.");
            throw new CustomException(400, "비밀번호가 일치하지 않습니다.");
        }

        if (member.getGroup() == null) {
            //ResponseMessage message = new ResponseMessage(400, "비밀번호가 일치 하지 않습니다.");
            throw new CustomException(401, "그룹이 존재하지 않습니다.");
        }

        String accessToken = tokenProvider.createAccessToken(member);
        //ResponseMessage message = new ResponseMessage(200, "로그인이 완료 되었습니다.", accessToken);
        return accessToken;
    }

    @Transactional
    public MemberInfoDTO getMemberInfo(Long memberId) {
        Optional<Member> byId = memberRepository.findById(memberId);

        if (byId.isEmpty()) {
            //ResponseMessage message = new ResponseMessage(404, "회원 정보가 존재하지 않습니다.");
            throw new CustomException(404, "회원정보가 존재하지 않습니다.");
        }

        Member member = byId.get();

        List<Project> projectList = memberProjectRepository.findProjectByMemberId(memberId, "true");

        return MemberInfoDTO.builder()
                .name(member.getUsername())
                .nickname(member.getNickname())
                .email(member.getEmail())
                .groupName(member.getGroup().getName())
                .position(member.getPosition())
                .introduce(member.getIntroduce())
                .projectList(projectList)
                .build();
    }

    @Transactional
    public MemberGroupDTO getMemberInfoWithGroupMember(Long memberId) {
        Optional<Member> byId = memberRepository.findById(memberId);

        if (byId.isEmpty()) {
            //ResponseMessage message = new ResponseMessage(404, "회원 정보가 존재하지 않습니다.");
            throw new CustomException(404, "회원정보가 존재하지 않습니다.");
        }

        Member member = byId.get();
        List<Member> groupMemberList = memberRepository.findByGroupIdAndIsActiveTrue(member.getGroup().getId());
        if (groupMemberList.isEmpty()) {
            throw new CustomException(404, "그룹의 회원이 존재하지 않습니다.");
        }

        List<MemberInfoDTO> memberInfoDTOList = new ArrayList<>();
        for (Member groupMember : groupMemberList) {
            memberInfoDTOList.add(
                    MemberInfoDTO.builder()
                            .id(groupMember.getId())
                            .name(groupMember.getUsername())
                            .nickname(groupMember.getNickname())
                            .email(groupMember.getEmail())
                            .position(groupMember.getPosition())
                            .authority(groupMember.getAuthority())
                            .build()
            );
        }

        System.out.println(memberInfoDTOList);
        return MemberGroupDTO.builder()
                .name(member.getUsername())
                .nickname(member.getNickname())
                .email(member.getEmail())
                .groupCode(member.getGroup().getCode())
                .groupName(member.getGroup().getName())
                .position(member.getPosition())
                .introduce(member.getIntroduce())
                .groupMember(memberInfoDTOList)
                .build();
    }

    Boolean checkPassword(String rawPassword, String encodePassword) {

        return passwordEncoder.matches(rawPassword, encodePassword);
    }

    @Transactional
    public void updateMember(Long id, MemberUpdateDTO memberUpdateDTO) {
        Optional<Member> byId = memberRepository.findById(id);
        Member member = byId.get();
        member.update(memberUpdateDTO.getIntroduce(), memberUpdateDTO.getNickname(), memberUpdateDTO.getPosition());

    }

    @Transactional
    public void removeMemberGroup(MemberInfoDTO memberInfoDTO){
        Optional<Member> byId = memberRepository.findById(memberInfoDTO.getId());
        Member member = byId.get();
        member.deleteGroupMember();
    }

    public ResponseEntity<?> getMemberOfProject(Long projectId) {
        List<MemberProject> memberByProjectId = memberProjectRepository.findMemberProjectByProjectId(projectId);
        if (memberByProjectId.isEmpty()) {
            ResponseMessage message = new ResponseMessage(404, projectId+"번 프로젝트에 해당하는 회원이 존재하지 않습니다.");
            return new ResponseEntity<>(message,HttpStatus.OK);
        }



        List<MemberInfoDTO> memberInfoDTOList = new ArrayList<>();

            for (MemberProject memberProject : memberByProjectId) {
                MemberInfoDTO memberInfoDTO = MemberInfoDTO.builder()
                        .id(memberProject.getId())
                        .name(memberProject.getMember().getUsername())
                        .nickname(memberProject.getMember().getNickname())
                        .email(memberProject.getMember().getEmail())
//                        .groupName(member.getGroup().getName())
                        .position(memberProject.getMember().getPosition())
                        .createdAt(memberProject.getMember().getCreatedAt())
                        .role(memberProject.getRole())
//                        .projectList(memberProjectRepository.findProjectByMemberId(memberProject.getId(),"true"))
                        .build();
                memberInfoDTOList.add(memberInfoDTO);
            }
        ResponseMessage message = new ResponseMessage(200, projectId+"번 프로젝트의 회원 정보 조회 완료", memberInfoDTOList);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }
}
