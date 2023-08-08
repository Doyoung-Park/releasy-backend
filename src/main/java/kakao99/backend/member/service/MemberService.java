package kakao99.backend.member.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.*;
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
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;


import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberService {
    private final ResourceLoader resourceLoader;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;
    private final GroupRepository groupRepository;
    private final MemberProjectRepository memberProjectRepository;
    private final RedisTemplate<String, String> redisTemplate;
    private static final  String absolutePath = "C:\\Users\\USER\\Desktop\\releasy_be\\BackEnd\\src\\main\\resources\\static\\";

    @Value("${kakao.i.cloud.access.token}")
    private String kakaoICloudAccessToken;

    @Value("${kakao.i.cloud.project.id}")
    private String projectID;
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
                .exp(0L)
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
                .exp(0L)
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
        String key = String.valueOf(member.getId());
        String value = "Online";
        redisTemplate.opsForValue().set(key, value, 10, TimeUnit.MINUTES);
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
                .id(member.getId())
                .name(member.getUsername())
                .nickname(member.getNickname())
                .email(member.getEmail())
                .groupName(member.getGroup().getName())
                .position(member.getPosition())
                .introduce(member.getIntroduce())
                .groupCode(member.getGroup().getCode())
                .projectList(projectList)
                .exp(member.getExp())
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
                            .introduce(groupMember.getIntroduce())
                            .name(groupMember.getUsername())
                            .nickname(groupMember.getNickname())
                            .email(groupMember.getEmail())
                            .position(groupMember.getPosition())
                            .authority(groupMember.getAuthority())
                            .createdAt(groupMember.getCreatedAt())
                            .build()
            );
        }

        System.out.println(memberInfoDTOList);
        return MemberGroupDTO.builder()
                .id(member.getId())
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
                String memberIdKey = String.valueOf(memberProject.getMember().getId());
                System.out.println("key"+memberIdKey);
                Boolean isOnline = redisTemplate.hasKey(memberIdKey);
                if(isOnline) {
                    System.out.println("11111");
                    MemberInfoDTO memberInfoDTO = MemberInfoDTO.builder()
                            .id(memberProject.getId())
                            .status("online")
                            .name(memberProject.getMember().getUsername())
                            .nickname(memberProject.getMember().getNickname())
                            .email(memberProject.getMember().getEmail())
//                        .groupName(member.getGroup().getName())
                            .position(memberProject.getMember().getPosition())
                            .createdAt(memberProject.getMember().getCreatedAt())
                            .role(memberProject.getRole())
//                        .projectList(memberProjectRepository.findProjectByMemberId(memberProject.getId(),"true"))
                          .exp(memberProject.getMember().getExp())
                            .build();

                    memberInfoDTOList.add(memberInfoDTO);
                }else {
                    System.out.println("22222");
                    MemberInfoDTO memberInfoDTO = MemberInfoDTO.builder()
                            .id(memberProject.getId())
                            .status("offline")
                            .name(memberProject.getMember().getUsername())
                            .nickname(memberProject.getMember().getNickname())
                            .email(memberProject.getMember().getEmail())
//                        .groupName(member.getGroup().getName())
                            .position(memberProject.getMember().getPosition())
                            .createdAt(memberProject.getMember().getCreatedAt())
                            .role(memberProject.getRole())
//                        .projectList(memberProjectRepository.findProjectByMemberId(memberProject.getId(),"true"))
                         .exp(memberProject.getMember().getExp())
                            .build();

                    memberInfoDTOList.add(memberInfoDTO);
                }

            }
        ResponseMessage message = new ResponseMessage(200, projectId+"번 프로젝트의 회원 정보 조회 완료", memberInfoDTOList);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    public void saveImage(Authentication authentication, MultipartFile profileImg) {
        Member member = (Member) authentication.getPrincipal();
        String fileName = String.valueOf(member.getId());
        System.out.println(profileImg.getOriginalFilename());
        System.out.println("Received image: " + fileName);
        try {

            // Set the endpoint URL for object storage and the HTTP method (PUT)
            String endpointUrl = "https://objectstorage.kr-gov-central-1.kakaoicloud-kr-gov.com/v1/" + projectID + "/" + "releasy" + "/profile/" + fileName;

            // Set the access token in the request header
            HttpHeaders headers = new HttpHeaders();
            headers.set("X-Auth-Token", kakaoICloudAccessToken);

            // Set the Content-Type header based on the file type
            headers.setContentType(MediaType.IMAGE_JPEG);

            // Get the image data from the MultipartFile without saving it to a file
            byte[] imageData = profileImg.getBytes();

            // Set the Content-Length header with the image data length
            headers.setContentLength(imageData.length);

            // Create the HTTP entity with headers and the image data
            HttpEntity<byte[]> requestEntity = new HttpEntity<>(imageData, headers);

            // Send the HTTP PUT request to upload the image to object storage
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> response = restTemplate.exchange(endpointUrl, HttpMethod.PUT, requestEntity, String.class);

            // Handle the response, e.g., check the status code, etc.
            if (response.getStatusCode().is2xxSuccessful()) {
                System.out.println("Image uploaded successfully!");
            } else {
                System.out.println("Image upload failed! Status code: " + response.getStatusCodeValue());
            }
        } catch (HttpServerErrorException e) {
            // 서버에서 발생한 500 Internal Server Error를 처리
            System.out.println("Internal Server Error occurred! Status code: " + e.getRawStatusCode());
            // 오류 메시지를 로깅 또는 사용자에게 알림으로 전달하는 로직 추가
            System.out.println("Error Response Body: " + e.getResponseBodyAsString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
