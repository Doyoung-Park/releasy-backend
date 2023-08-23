package kakao99.backend.release.controller;

import kakao99.backend.common.exception.CustomException;
import kakao99.backend.entity.Member;
import kakao99.backend.entity.Project;
import kakao99.backend.entity.ReleaseNote;
import kakao99.backend.member.repository.MemberRepository;
import kakao99.backend.project.repository.ProjectRepository;

import kakao99.backend.release.dto.CreateReleaseDTO;
import kakao99.backend.release.dto.GetReleaseDTO;
import kakao99.backend.release.dto.GetReleaseListDTO;
import kakao99.backend.release.dto.UpdateReleaseDTO;
import kakao99.backend.release.service.NoteTreeService;
import kakao99.backend.release.service.ReleaseService;
import kakao99.backend.common.ResponseMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@Slf4j
@RequiredArgsConstructor
public class ReleaseController {
    private final ReleaseService releaseService;
    private final MemberRepository memberRepository;
    private final ProjectRepository projectRepository;

    private final NoteTreeService noteTreeService;

    @PostMapping(value = "/api/release/create")
    @ResponseBody
    public ResponseEntity<ResponseMessage> createReleaseNote(Authentication authentication, @RequestPart(value = "jsonData") CreateReleaseDTO createReleaseDTO,
                                                             @RequestPart(value="image", required=false) List<MultipartFile> files) throws IOException {
        log.info("릴리즈 노트 생성 요청");
        ReleaseNote releaseNote = null;
        Member member = (Member) authentication.getPrincipal();
        Optional<Project> project = projectRepository.findById(createReleaseDTO.getProjectId());
        if (project.isEmpty()) {
            ResponseMessage message = new ResponseMessage(204, "멤버 또는 프로젝트를 찾을 수 없습니다.", null);
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        }

        if (files == null) {
            log.info("전송받은 사진 개수: 0개");
            releaseNote = releaseService.createReleaseWithoutImages(createReleaseDTO, member, project.get());
        }else{
            log.info("전송 받은 사진 개수 = " + files.toArray().length);
            if (files.toArray().length > 3) {
                log.warn("전송 받은 사진 개수가 3개 이상. Exception! ");
                throw new CustomException(999, "사진 데이터는 최대 3개까지만 가능합니다.");
            }
            releaseNote = releaseService.createReleaseWithImages(createReleaseDTO, member, project.get(), files);
        }

        if (releaseNote == null) {
            log.info("릴리즈 노트 생성 실패");
            ResponseMessage message = new ResponseMessage(500, "릴리즈 생성에 실패했습니다.", null);
            return new ResponseEntity<>(message, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        Long newReleaseNoteId = releaseNote.getId();

        releaseService.updateIssues(newReleaseNoteId, createReleaseDTO.getIssueList());
        log.info("릴리즈 노트 생성 완료");
        ResponseMessage message = new ResponseMessage(200, "릴리즈 생성 완료", releaseNote);
        return new ResponseEntity<>(message, HttpStatus.OK);

    }

    @PutMapping("/api/release/update")
    @ResponseBody
    public ResponseEntity<ResponseMessage> updateReleaseNote(
            @RequestBody UpdateReleaseDTO updateReleaseDTO) {
        Optional<ReleaseNote> findReleaseNote = releaseService.getReleaseInfo(updateReleaseDTO.getReleaseId());

        if (findReleaseNote.isEmpty()) {
            ResponseMessage message = new ResponseMessage(204, "릴리즈 노트를 찾을 수 없습니다.", null);
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        }

        releaseService.updateRelease(updateReleaseDTO);

        releaseService.updateIssues(updateReleaseDTO.getReleaseId(), updateReleaseDTO.getIssueList());

        ResponseMessage message = new ResponseMessage(200, "릴리즈 업데이트 완료", null);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @GetMapping("/api/release")
    @ResponseBody
    public ResponseEntity<ResponseMessage> getReleaseNoteList (
            @RequestParam(required = true, value = "projectId") Long projectId
    ) {
        List<ReleaseNote> releaseNotesInProject = releaseService.findRelease(projectId);

        List<GetReleaseListDTO> releaseList = new ArrayList<>();

        for (ReleaseNote releaseNote : releaseNotesInProject) {
            GetReleaseListDTO releaseDTO = new GetReleaseListDTO();
            releaseDTO.setId(releaseNote.getId());
            releaseDTO.setVersion(releaseNote.getVersion());
            releaseDTO.setStatus(releaseNote.getStatus());
            releaseDTO.setReleaseDate(releaseNote.getReleaseDate());
            releaseDTO.setCreatedAt(releaseNote.getCreatedAt());
            releaseDTO.setMember(memberRepository.findById(releaseNote.getMember().getId()).get());
            releaseList.add(releaseDTO);
        }

        ResponseMessage message;

        if (releaseList.isEmpty()) {
            message = new ResponseMessage(204, "본 프로젝트에 속한 릴리즈노트가 없습니다.", null);
            return new ResponseEntity<>(message, HttpStatus.NO_CONTENT);
        }

        message = new ResponseMessage(200, "릴리즈노트 목록 조회 완료", releaseList);

        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @GetMapping("/api/release/{releaseId}")
    @ResponseBody
    public ResponseEntity<ResponseMessage> getReleaseNoteInfo(@PathVariable Long releaseId) {

        Optional<ReleaseNote> releaseNoteInfo = releaseService.getReleaseInfo(releaseId);
        ResponseMessage message;

        if (releaseNoteInfo.isEmpty()) {
            message = new ResponseMessage(204, "해당 릴리즈노트가 존재하지 않습니다.", null);
            return new ResponseEntity<>(message, HttpStatus.NO_CONTENT);
        }

        ReleaseNote releaseNote = releaseNoteInfo.get();

        GetReleaseDTO releaseDTO = new GetReleaseDTO();
        releaseDTO.setVersion(releaseNote.getVersion());
        releaseDTO.setStatus(releaseNote.getStatus());
        releaseDTO.setPercent(releaseNote.getPercent());
        releaseDTO.setReleaseDate(releaseNote.getReleaseDate());
        releaseDTO.setCreatedAt(releaseNote.getCreatedAt());
        releaseDTO.setBrief(releaseNote.getBrief());
        releaseDTO.setDescription(releaseNote.getDescription());
        releaseDTO.setImgUrl_1(releaseNote.getImgUrl_1());
        releaseDTO.setImgUrl_2(releaseNote.getImgUrl_2());
        releaseDTO.setImgUrl_3(releaseNote.getImgUrl_3());
        releaseDTO.setMember(memberRepository.findById(releaseNote.getMember().getId()).get());

        message = new ResponseMessage(200, "해당 릴리즈노트 조회 완료", releaseDTO);

        return new ResponseEntity<>(message, HttpStatus.OK);
    }


    @DeleteMapping("/api/release/{releaseId}")
    @ResponseBody
    public ResponseEntity<ResponseMessage> deleteRelease(@PathVariable("releaseId") Long releaseId) {
        log.info("삭제 컨트롤러 시작");

        releaseService.deleteRelease(releaseId);

        ResponseMessage message = new ResponseMessage(200, "삭제 완료");
        return new ResponseEntity<>(message, HttpStatus.OK);
    }


    @GetMapping("/api/{projectId}/releases/tree")
    public ResponseEntity<ResponseMessage> getTree(@PathVariable("projectId") Long projectId){
        noteTreeService.getTreesForProject(projectId);

        ResponseMessage message = new ResponseMessage(200, "트리 생성 완료", noteTreeService.getTreesForProject(projectId));

        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @PostMapping("api/releaseNote/{releaseNoteId}/images")
    public ResponseEntity<?> updateImageAboutReleaseNote(@PathVariable("releaseNoteId") Long releaseNoteId, @RequestPart(value="image", required=false) List<MultipartFile> files ) throws IOException {
        log.info("이슈 관련 이미지 수정");

        if (files == null)
            log.info("전송 받은 사진 개수 = 0");
        else
            log.info("전송 받은 사진 개수 = " + files.toArray().length);

        releaseService.saveImageAboutReleaseNote(releaseNoteId, files);

        ResponseMessage message = new ResponseMessage(200, "이미지 수정 완료");
        return new ResponseEntity<>(message, HttpStatus.OK);
    }
}


