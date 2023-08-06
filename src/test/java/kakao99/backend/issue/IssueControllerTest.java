package kakao99.backend.issue;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import kakao99.backend.common.ResponseMessage;
import kakao99.backend.entity.Issue;
import kakao99.backend.issue.controller.IssueController;
import kakao99.backend.issue.controller.IssueForm;
import kakao99.backend.issue.repository.IssueParentChildRepository;
import kakao99.backend.issue.repository.IssueRepository;
import kakao99.backend.issue.service.IssueService;
import kakao99.backend.issue.service.TreeService;
import kakao99.backend.jwt.JwtFilter;
import kakao99.backend.jwt.TokenProvider;
import kakao99.backend.member.repository.MemberRepository;
import kakao99.backend.notification.service.NotificationService;
import kakao99.backend.project.repository.ProjectRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.test.web.servlet.*;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Optional;

import static org.springframework.http.RequestEntity.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

//@SpringBootTest

@WebMvcTest(controllers = {IssueController.class})
@AutoConfigureMockMvc
//@PropertySource("classpath:application.properties")
public class IssueControllerTest {


    @Value("${chatGptSecretKey}")
    private String chatGptSecretKey;

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    private WebApplicationContext context;


    // === 테스트
    @MockBean
    IssueRepository issueRepository;

    @MockBean
    IssueService issueService;

    @MockBean
    IssueParentChildRepository issueParentChildRepository;

    @MockBean
    TreeService treeService;

    @MockBean
    MemberRepository memberRepository;

    @MockBean
    ProjectRepository projectRepository;

    @MockBean
    NotificationService notificationService;

    // ===
    /*

    private final IssueRepository issueRepository;
    private final IssueParentChildRepository issueParentChildRepository;
    private final IssueService issueService;
    private final TreeService treeService;
    private final MemberRepository memberRepository;
    private final ProjectRepository projectRepository;
    private final NotificationService notificationService;
     */

    @BeforeEach
    public void mockMvcSetUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context).build();

    }

//    @BeforeEach
//    public void mockMvcSetUp() {
//        this.mockMvc = MockMvcBuilders.standaloneSetup(issueController).build();
//    }

    @Test
    public void 이슈_생성_테스트() throws Exception {
        final Long projectId = 1L;
        final String title = "22이게 맞나 테스트";
        final String writerName = "박도영";
        final String type = "진행중?";
        final String description = "테스트입니다.";
        final Long userId = 1L;

        final String url = (String) "/api/project/" + projectId + "/issue";

        IssueForm issueForm = IssueForm.builder()
                .title(title)
                .writerName(writerName)
                .type(type)
                .description(description)
                .userId(userId)
                .build();

        final String requestBody = objectMapper.writeValueAsString(issueForm);

        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody));

        result.andExpect(status().isOk());
        result.andDo(MockMvcResultHandlers.print());

//        resetTestData();
    }


//    public void resetTestData() {
//        Long maxId = issueRepository.findMaxId();
//        System.out.println("maxId = " + maxId);
//        issueRepository.deleteById(maxId);
//    }

    @Test
    public void ChatGPT_이슈중요도_추천() throws Exception {
        // given
        final Long projectId = 1L;
        final String url = "/api/project/" + projectId + "/importance";

        // when
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.get(url)
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + chatGptSecretKey)
        );

        // then
        result.andExpect(status().isOk());
        result.andDo(print());

        result.andExpect(jsonPath("$.statusCode").value(200));
        result.andExpect(jsonPath("$.message").value("GPT 중요도 추천이 완료되었습니다."));
    }


}
