package kakao99.backend.release.controller;

import kakao99.backend.entity.Member;
import kakao99.backend.entity.Project;
import kakao99.backend.entity.ReleaseNote;
import kakao99.backend.member.repository.MemberRepository;
import kakao99.backend.project.repository.ProjectRepository;

import kakao99.backend.release.dto.CreateReleaseDTO;
import kakao99.backend.release.dto.GetReleaseDTO;
import kakao99.backend.release.dto.GetReleaseListDTO;
import kakao99.backend.release.dto.UpdateReleaseDTO;
import kakao99.backend.release.service.ReleaseService;
import kakao99.backend.common.ResponseMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/api/release/create")
    @ResponseBody
    public ResponseEntity<ResponseMessage> createRelease(Authentication authentication, @RequestBody CreateReleaseDTO createReleaseDTO) {

        // member와 project를 조회
        Member member = (Member) authentication.getPrincipal();
        Optional<Project> project = projectRepository.findById(createReleaseDTO.getProjectId());

        if (project.isEmpty()) {
            ResponseMessage message = new ResponseMessage(204, "멤버 또는 프로젝트를 찾을 수 없습니다.", null);
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        }
        ReleaseNote releaseNote = releaseService.createRelease(createReleaseDTO, member, project.get());

        if (releaseNote == null) {
            ResponseMessage message = new ResponseMessage(500, "릴리즈 생성에 실패했습니다.", null);
            return new ResponseEntity<>(message, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        Long newId = releaseNote.getId();

        releaseService.updateIssues(newId, createReleaseDTO.getIssueList());

        ResponseMessage message = new ResponseMessage(200, "릴리즈 생성 완료", releaseNote);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @PutMapping("/api/release/update")
    @ResponseBody
    public ResponseEntity<ResponseMessage> updateRelease(
            @RequestBody UpdateReleaseDTO updateReleaseDTO) {
        Optional<ReleaseNote> findReleaseNote = releaseService.getReleaseInfo(updateReleaseDTO.getReleaseId());

        if (findReleaseNote.isEmpty()) {
            ResponseMessage message = new ResponseMessage(204, "릴리즈 노트를 찾을 수 없습니다.", null);
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        }

        releaseService.updateRelease(updateReleaseDTO.getReleaseId(), updateReleaseDTO.getVersion(), updateReleaseDTO.getStatus(),
                updateReleaseDTO.getPercent(), updateReleaseDTO.getReleaseDate(), updateReleaseDTO.getBrief(), updateReleaseDTO.getDescription());

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

}


