package kakao99.backend.issue.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.*;
import com.nimbusds.common.contenttype.ContentType;
import jakarta.persistence.Table;
import kakao99.backend.common.exception.CustomException;
import kakao99.backend.entity.Issue;
import kakao99.backend.entity.Member;
import kakao99.backend.entity.Notification;
import kakao99.backend.issue.controller.UpdateIssueForm;
import kakao99.backend.issue.dto.DragNDropDTO;

import kakao99.backend.issue.dto.IssueDTO;
import kakao99.backend.issue.dto.ProjectWithIssuesDTO;
import kakao99.backend.issue.repository.IssueParentChildRepository;

import kakao99.backend.issue.dto.*;

import kakao99.backend.issue.repository.IssueRepository;
import kakao99.backend.issue.repository.IssueRepositoryImpl;
import kakao99.backend.member.repository.MemberRepository;
import kakao99.backend.notification.service.NotificationService;
import kakao99.backend.project.service.ProjectService;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import org.apache.tomcat.util.json.JSONParser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@PropertySource("classpath:application.properties")
@Transactional(readOnly = true)
public class IssueService {
    private final IssueRepository issueRepository;
    private final MemberRepository memberRepository;
    private final ProjectService projectService;
    private final NotificationService notificationService;

    private final IssueRepositoryImpl issueRepositoryImpl;

    @Value("${chatGptSecretKey}")
    private String chatGptSecretKey;


    private final IssueParentChildRepository issueParentChildRepository;

    public List<Issue> getIssuesWithMemo(Long projectId) {
        return issueRepository.findAllByProjectId(projectId);
    }


    public boolean isChildIssue(Issue issue) {
        return issueParentChildRepository.existsByChildIssue(issue);
    }

    public List<IssueDTO> getAllIssues(Long projectId) {

        List<Issue> issueList = issueRepository.findAllByProjectId(projectId);
        System.out.println("allIssueByProjectId.toArray().length = " + issueList.toArray().length);
        List<IssueDTO> issueDTOListFromIssueList = IssueDTO.getIssueDTOListFromIssueList(issueList);
        return issueDTOListFromIssueList;
    }




    public List<IssueDTO> getAllIssuesByFilter(Long projectId ,String status, String type, String name) {

        List<Issue> allIssueByProjectId = issueRepositoryImpl.findAllWithFilter(projectId, status, type, name);


        return allIssueByProjectId.stream().map(issue -> {
            // Call the existsByChildIssue here
            boolean isChild = issueParentChildRepository.existsByChildIssue(issue);

            // Pass the isChild value to the DTO
            IssueDTO issueDTO = IssueDTO.fromIssueAndIsChild(issue, isChild);

            return issueDTO;
        }).collect(Collectors.toList());
    }

    public List<IssueDTO> getAllIssuesWithoutexcludeId(Long projectId , Long excludeId) {

        List<Long>  issueParentChildId = issueRepositoryImpl.findExcludeId(projectId,excludeId);

        List<Issue> allIssueByProjectId = issueRepositoryImpl.findWithoutExcludeId(projectId,issueParentChildId);

        List<IssueDTO> issueDTOListFromIssueList = IssueDTO.getIssueDTOListFromIssueList(allIssueByProjectId);

        return issueDTOListFromIssueList;
    }


    @Transactional
    public void updateIssue(UpdateIssueForm updateIssueForm, Long issueId) {
        issueRepositoryImpl.updateIssue(updateIssueForm, issueId);
        }

        public List<IssueDTO> getIssueListIncludedInReleaseNote(Long releaseNoteId) {
            List<Issue> issueListIncludedInReleaseNote = issueRepository.findAllByReleaseNoteId(releaseNoteId);

            if (issueListIncludedInReleaseNote == null || issueListIncludedInReleaseNote.isEmpty()) {
                throw new NoSuchElementException("릴리즈 노트에 포함된 이슈가 없습니다.");
            }
            List<IssueDTO> issueDTOListFromIssueList = IssueDTO.getIssueDTOListFromIssueList(issueListIncludedInReleaseNote);
            return issueDTOListFromIssueList;
        }


    public List<IssueDTO> getIssueListNotIncludedInReleaseNote(Long projectId) {
        List<Issue> issueListNotIncludedInReleaseNote = issueRepository.findAllByNotReleaseNoteId(projectId);

        if (issueListNotIncludedInReleaseNote == null || issueListNotIncludedInReleaseNote.isEmpty()) {
            throw new NoSuchElementException("릴리즈 노트에 포함되지 않은 이슈가 없습니다.");
        }

        List<IssueDTO> issueDTOListFromIssueList = IssueDTO.getIssueDTOListFromIssueList(issueListNotIncludedInReleaseNote);
       return issueDTOListFromIssueList;
    }

    // issue management 페이지에서 필요한 데이터 get
    public ProjectWithIssuesDTO getIssueManagementPageData(Long projectId) {
        // 프로젝트 info
        ProjectWithIssuesDTO projectInfo = projectService.getProjectIdAndName(projectId);

        // issue List
        List<IssueDTO> allIssues = getAllIssues(projectId);
        projectInfo.saveIssueList(allIssues);

        // memo

        return projectInfo;
    }

    @Transactional
    public Long deleteIssue(Long issueId, Long memberId) {
        Optional<Issue> issueByIssueId = issueRepository.findIssueById(issueId);
        if (issueByIssueId.isEmpty()) {
            throw new CustomException(404, issueByIssueId + "번 이슈가 존재하지 않습니다.");
        }

        issueRepository.deleteIssue(issueId, memberId);

        return issueId;
    }


    @Transactional
    public Long deleteChildIssue(Long issueId, Long childIssueId) {
        Optional<Issue> issueByIssueId = issueRepository.findIssueById(issueId);
        if (issueByIssueId.isEmpty()) {
            throw new CustomException(404, issueByIssueId + "번 이슈가 존재하지 않습니다.");
        }

        issueRepositoryImpl.deleteChild(issueId, childIssueId);

        return issueId;
    }



    @Transactional
    public void updateIssueByDragNDrop(DragNDropDTO dragNDropDTO, Long userId) {
        issueRepository.updateIssueByDragNDrop(dragNDropDTO);
        Optional<Member> optionalMember = memberRepository.findById(userId);
        if (optionalMember.isEmpty()) {
            throw new NoSuchElementException("해당 멤버가 존재하지 않습니다.");
        }

        Long issueId = dragNDropDTO.getIssueId();
        Optional<Issue> issueOptional = issueRepository.findIssueById(issueId);
        if (issueOptional.isEmpty()) {
            throw new CustomException(404, issueId + "번 이슈가 존재하지 않습니다.");
        }
        Issue issue = issueOptional.get();
        Member memberReport = optionalMember.get();
        Notification notification = notificationService.createNotification(dragNDropDTO, memberReport, issue);
    }

    public List<GPTQuestionDTO> askImportanceToGPT(Long projectId){
        List<Issue> issueListNotFinished = issueRepository.getIssueListNotFinishedOf(projectId);
        List<GPTQuestionDTO> questionList = GPTQuestionDTO.organizeIssueListIntoQuestion(issueListNotFinished);
        List<GPTQuestionDTO> gptQuestionDTOList = sendIssueListToGPT(questionList);
        return gptQuestionDTOList;
    }

    public List<GPTQuestionDTO> sendIssueListToGPT(List<GPTQuestionDTO> questionList){
        String apiUrl = "https://api.openai.com/v1/chat/completions";
        RestTemplate rt = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer "+ chatGptSecretKey);

        String QuestionList = "";
        for (GPTQuestionDTO GptQuestion : questionList) {
            QuestionList += "id:"+GptQuestion.getId()+ "- question: "+GptQuestion.getQuestion()+", ";
        }

        QuestionList +=" 너가 이 작업들의 중요도를 임의로 0과 100 사이의 숫자로 정하고, 그 값만 딱 알려줘. 답변은 무조건 다른 말 아무것도 없이 json형식으로 표시해줘. json 형식은 {id값: 중요도값, } 으로 정해서 표시해줘.";

        String requestBody = "{\"model\": \"gpt-3.5-turbo\", \"messages\": [{\"role\": \"system\", \"content\": \"You are a helpful assistant.\"}, {\"role\": \"user\", \"content\": " +
                "\"" + QuestionList + "\"}]}";

        JsonParser jsonParser = new JsonParser();
        JsonObject parse = (JsonObject) jsonParser.parse(requestBody);

        Gson gson = new Gson();
        String json = gson.toJson(parse);

//      Set http entity -> Body 데이터와 헤더 묶기
        HttpEntity<String> stringHttpEntity = new HttpEntity<>(json, headers);
        ResponseEntity<ChatGptResponse> exchange = rt.exchange(apiUrl, HttpMethod.POST, stringHttpEntity, ChatGptResponse.class);

            // API 응답 데이터에서 "message" 필드만 추출하여 반환
        String response = exchange.getBody().getChoices().get(0).getMessage().getContent();

        JsonObject gptSentResult = (JsonObject)jsonParser.parse(response);
        log.info("gptSentResult = " + gptSentResult);
        for (GPTQuestionDTO GptQuestion : questionList) {
            String questionId = Long.toString(GptQuestion.getId());
            JsonElement jsonPoint = gptSentResult.get(questionId);
            int importance = Integer.parseInt(String.valueOf(jsonPoint));

            GptQuestion.setImportance(importance);
        }
        return questionList;
    }
}
