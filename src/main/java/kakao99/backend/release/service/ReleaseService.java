package kakao99.backend.release.service;

import kakao99.backend.common.exception.CustomException;
import kakao99.backend.entity.*;
import kakao99.backend.entity.types.NotificationType;
import kakao99.backend.issue.repository.IssueRepository;
import kakao99.backend.member.repository.MemberRepository;
import kakao99.backend.notification.rabbitmq.dto.RequestMessageDTO;
import kakao99.backend.notification.service.NotificationService;
import kakao99.backend.release.dto.CreateReleaseDTO;
import kakao99.backend.release.dto.UpdateReleaseDTO;
import kakao99.backend.release.repository.ReleaseParentChildRepository;
import kakao99.backend.release.repository.ReleaseRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class ReleaseService {

    private final ReleaseRepository releaseRepository;
    private final MemberRepository memberRepository;
    private final IssueRepository issueRepository;
    private final NotificationService notificationService;

    private final ReleaseParentChildRepository releaseParentChildRepository;

    @Value("${kakao.i.cloud.access.token}")
    private String kakaoICloudAccessToken;

    @Value("${kakao.i.cloud.project.id}")
    private String KicProjectID;

    @Value("${kakao.i.cloud.object.storage.url}")
    private String kicObjectStorageUrl;

    @Transactional
    public ReleaseNote createReleaseWithoutImages(CreateReleaseDTO createReleaseDTO, Member member, Project project) {

        ReleaseNote newReleaseNote = ReleaseNote.builder()
                .version(createReleaseDTO.getVersion())
                .status(createReleaseDTO.getStatus())
                .percent(createReleaseDTO.getPercent())
                .releaseDate(createReleaseDTO.getReleaseDate())
                .brief(createReleaseDTO.getBrief())
                .description(createReleaseDTO.getDescription())

                .isActive(true)
                .member(member)
                .project(project)
                .deletedAt(null)
                .build();

        releaseRepository.save(newReleaseNote);

        RequestMessageDTO requestMessageDTO = new RequestMessageDTO().builder()
                .type(NotificationType.RELEASENOTECREATED)
                .specificTypeId(newReleaseNote.getId())
                .projectId(newReleaseNote.getProject().getId())
                .build();

        notificationService.createNotification(requestMessageDTO);

        // Then retrieve parent note based on versioning rule and create parent-child relationship if parent note exists
        ReleaseNote parentNote = getParentNoteBasedOnVersion(createReleaseDTO.getVersion(), project);
        if(parentNote != null) {
            ReleaseNoteParentChild releaseNoteParentChild = ReleaseNoteParentChild.createReleaseNoteParentChild(parentNote, newReleaseNote, new Date());
            releaseParentChildRepository.save(releaseNoteParentChild);
        }

        return newReleaseNote;
    }

    @Transactional
    public ReleaseNote createReleaseWithImages(CreateReleaseDTO createReleaseDTO, Member member, Project project, List<MultipartFile> files) throws IOException {

        ArrayList<String> imgUrlList = new ArrayList<>();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);
        headers.set("X-Auth-Token", kakaoICloudAccessToken);
        RestTemplate restTemplate = new RestTemplate();

            // 다중 파일 처리
            for (MultipartFile file : files) {
                String endpointUrl = kicObjectStorageUrl+ KicProjectID;
                String imgUrlSample ="/releasy" + "/releaseNote/";
//                String originalFileName = file.getOriginalFilename();

                String uuid = UUID.randomUUID().toString();
//                String newFileName = uuid + "_" + originalFileName;
                String newFileName = uuid + "_" + "index.jpeg";
                imgUrlSample += newFileName;
                endpointUrl += imgUrlSample;

                imgUrlList.add(imgUrlSample);

                byte[] imageData = file.getBytes();

                HttpEntity<byte[]> requestEntity = new HttpEntity<>(imageData, headers);

                ResponseEntity<String> response = restTemplate.exchange(endpointUrl, HttpMethod.PUT, requestEntity, String.class);
                if (response.getStatusCode().is2xxSuccessful()) {
                    System.out.println("Image uploaded successfully!");
                } else {
                    System.out.println("Image upload failed! Status code: " + response.getStatusCodeValue());
                }
            }

        ReleaseNote newReleaseNoteWithImage = releaseRepository.createReleaseNoteWithImage(createReleaseDTO, member, project, imgUrlList);
        releaseRepository.save(newReleaseNoteWithImage);
        return newReleaseNoteWithImage;

    }
    private ReleaseNote getParentNoteBasedOnVersion(String version, Project project) {
        String[] versions = version.split("\\.");
        if (versions.length != 3) {
            throw new IllegalArgumentException("Version should be in the format of x.y.z");
        }

        int major = Integer.parseInt(versions[0]);
        int minor = Integer.parseInt(versions[1]);
        int patch = Integer.parseInt(versions[2]);

        if(patch > 0) {
            //패치가 생성되면 maior.minor.0을 상위 릴리즈 노트로 설정
            String parentVersion = major + "." + minor + "." + "0";
            return releaseRepository.findByVersionAndProject(parentVersion, project);
        }else{
            return null;
        }

    }

    public List<ReleaseNote> findRelease(Long id) {
        // 해당 프로젝트 id에 맞는 릴리즈 노트 목록 가져오기
        return releaseRepository.findByProjectIdAndIsActiveTrue(id);
    }

    public Optional<ReleaseNote> getReleaseInfo(Long id) {
        // 릴리즈 노트 하나 선택하면 해당하는 릴리즈 노트 정보 가져오기
        return releaseRepository.findById(id);
    }


//            releaseService.updateRelease(updateReleaseDTO.getReleaseId(), updateReleaseDTO.getVersion(), updateReleaseDTO.getStatus(),
//                    updateReleaseDTO.getPercent(), updateReleaseDTO.getReleaseDate(), updateReleaseDTO.getBrief(), updateReleaseDTO.getDescription());

    @Transactional
    public void updateRelease(UpdateReleaseDTO updateReleaseDTO) {

        releaseRepository.updateReleaseNoteById(updateReleaseDTO.getReleaseId(), updateReleaseDTO.getVersion(),
                updateReleaseDTO.getStatus(), updateReleaseDTO.getPercent(), updateReleaseDTO.getReleaseDate(),
                updateReleaseDTO.getBrief(), updateReleaseDTO.getDescription());

        ReleaseNote releaseNote = releaseRepository.findById(updateReleaseDTO.getReleaseId()).get();



        RequestMessageDTO requestMessageDTO = new RequestMessageDTO().builder()
                .type(NotificationType.RELEASENOTECHANGED)
                .specificTypeId(updateReleaseDTO.getReleaseId())
                .projectId(releaseNote.getProject().getId())
                .build();

        notificationService.createNotification(requestMessageDTO);
    }

    @Transactional
    public void updateIssues(Long releaseId, List<Issue> newIssueList) {   // issueList : 결과물
        List<Issue> oldIssueListOfReleaseNote = issueRepository.findAllByReleaseNoteId(releaseId);

        // 추가
        for (Issue issue : newIssueList) {
            Long issueId = issue.getId();
            boolean foundInOldList = false;

            for (Issue oldIssue : oldIssueListOfReleaseNote) {
                if (issueId.equals(oldIssue.getId())) {
                    foundInOldList = true;
                    break;
                }
            }

            if (!foundInOldList) {
                Optional<Issue> existingIssue = issueRepository.findById(issueId); // Assuming you have an issueRepository to retrieve the existing issue by its ID
                int res = issueRepository.insertIssueFromReleaseNote(releaseId, existingIssue.get().getId()); // Assuming you have a method addReleaseNote in your Issue class to associate the releaseNote
            }
        }

        // 삭제
        for (Issue issue : oldIssueListOfReleaseNote) {
            Long issueId = issue.getId();
            boolean foundInNewList = false;

            for (Issue newIssue : newIssueList) {
                if (issueId.equals(newIssue.getId())) {
                    foundInNewList = true;
                    break;
                }
            }

            if (!foundInNewList) {
                Optional<Issue> deleteIssue = issueRepository.findById(issueId); // Assuming you have an issueRepository to retrieve the existing issue by its ID
                int res = issueRepository.deleteIssueFromReleaseNote(deleteIssue.get().getId()); // Assuming you have a method addReleaseNote in your Issue class to associate the releaseNote
            }
        }

        return;

    }

    @Transactional
    public void deleteRelease(Long releaseNoteId) {
        // 릴리즈 노트 아이디로 isActive를 False로 바꾸고 지운 시간 넣어주기
        releaseRepository.updateIsActiveById(releaseNoteId, new Date());

        ReleaseNote releaseNote = releaseRepository.findById(releaseNoteId).get();

        RequestMessageDTO requestMessageDTO = new RequestMessageDTO().builder()
                .type(NotificationType.RELEASENOTEDELETED)
                .specificTypeId(releaseNoteId)
                .projectId(releaseNote.getProject().getId())
                .build();

        notificationService.createNotification(requestMessageDTO);
    }

    @Transactional
    public void saveImageAboutReleaseNote(Long releaseNoteId, List<MultipartFile> files) throws IOException {
        ArrayList<String> imgUrlList = new ArrayList<>();

        if (files == null)
            releaseRepository.updateReleaseNoteImage(releaseNoteId, imgUrlList);

        // 전달되어 온 파일이 존재할 경우
        if(!CollectionUtils.isEmpty(files)) {
            HttpHeaders headers = new HttpHeaders();
            headers.set("X-Auth-Token", kakaoICloudAccessToken);
            headers.setContentType(MediaType.IMAGE_JPEG);
            RestTemplate restTemplate = new RestTemplate();

            log.info(String.valueOf(files.size()));
            // 다중 파일 처리
            for (MultipartFile file : files) {
                String endpointUrl = kicObjectStorageUrl+KicProjectID;
                String imgUrlSample ="/releasy" + "/issue/";

//                // 파일의 확장자 추출
//                String originalFileExtension;
//                String contentType = file.getContentType();
//
//                // 확장자명이 존재하지 않을 경우 처리 x
//                if (ObjectUtils.isEmpty(contentType)) {
//                    break;
//                } else {  // 확장자가 jpeg, png인 파일들만 받아서 처리
//                    if (contentType.contains("image/jpeg"))
//                        originalFileExtension = "jpg";
//                    else if (contentType.contains("image/png"))
//                        originalFileExtension = "png";
//                    else  // 다른 확장자일 경우 처리 x
//                        break;
//                }
//                String originalFileName = file.getOriginalFilename();

//                // 나노초를 문자열로 변환하여 출력
//                long nanoTime = System.nanoTime();
//                String nanoTimeString = String.valueOf(nanoTime);
//                System.out.println("nanoTimeString = " + nanoTimeString);
//                System.out.println("Nano Time as String: " + nanoTimeString);

                String uuid = UUID.randomUUID().toString();
                String newFileName = uuid + "_" + "index.jpeg";
//                String newFileName = nanoTimeString + "_" + originalFileName;

                imgUrlSample += newFileName;
                endpointUrl += imgUrlSample;

                imgUrlList.add(imgUrlSample);

                byte[] imageData = file.getBytes();

                HttpEntity<byte[]> requestEntity = new HttpEntity<>(imageData, headers);

                ResponseEntity<String> response = restTemplate.exchange(endpointUrl, HttpMethod.PUT, requestEntity, String.class);
                if (response.getStatusCode().is2xxSuccessful()) {
                    System.out.println("Image uploaded successfully!");
                } else {
                    System.out.println("Image upload failed! Status code: " + response.getStatusCodeValue());
                }
            }
            releaseRepository.updateReleaseNoteImage(releaseNoteId, imgUrlList);
        }
    }

}
