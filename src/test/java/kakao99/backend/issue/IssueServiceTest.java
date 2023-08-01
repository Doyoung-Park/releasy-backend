package kakao99.backend.issue;

import kakao99.backend.BackendApplication;
import kakao99.backend.config.TestConfig;
import kakao99.backend.entity.Issue;
import kakao99.backend.issue.dto.GPTQuestionDTO;
import kakao99.backend.issue.repository.IssueParentChildRepository;
import kakao99.backend.issue.repository.IssueRepository;
import kakao99.backend.issue.repository.IssueRepositoryImpl;
import kakao99.backend.issue.service.IssueService;
import kakao99.backend.member.repository.MemberRepository;
import kakao99.backend.notification.service.NotificationService;
import kakao99.backend.project.service.ProjectService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.hamcrest.Matchers.greaterThan;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;


//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)


@ExtendWith(MockitoExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
//@ContextConfiguration(classes = BackendApplication.class)
@Import(TestConfig.class)
//@ExtendWith(MockitoExtension.class)
public class IssueServiceTest {

    @InjectMocks
    IssueService issueService;

//    @Mock
    @Autowired
    IssueRepository issueRepository;
    @Mock
    MemberRepository memberRepository;
    @Mock
    ProjectService projectService;
    @Mock
    NotificationService notificationService;
    @Mock
    IssueRepositoryImpl issueRepositoryImpl;
    @Mock
    IssueParentChildRepository issueParentChildRepository;


//    @InjectMocks
//    private IssueService issueService;

//    @BeforeEach
//    public void beforeEach() {
//    issueRepository = new IssueRepository() {
//    }
//    }

    @Test
    void Chat_GPT_API_테스트() {
        System.out.println("hi");

//        issueRepository.getIssueListNotFinishedOf(projectId);
    }

    @Test
    void 이슈_조회() {

        Long issueId = 1L;

        Optional<Issue> issueOptional = issueRepository.findById(issueId);
        Issue issue = issueOptional.get();
        System.out.println("issue.getTitle() = " + issue.getTitle());
        assertEquals(issue.getId(), issueId);
    }


    @Test
    void 종료되지_않은_이슈_조회_테스트() {

        // given
        Long projectId = 1L;
        List<Issue> issueListNotFinished = issueRepository.getIssueListNotFinishedOf(projectId);    // 종료되지 않은 이슈
//        System.out.println("issueListNotFinished = " + issueListNotFinished);
        int notActiveIssueListLength = issueListNotFinished.stream().toArray().length;
        System.out.println("notActiveIssueListLength = " + notActiveIssueListLength);

        // when
        List<Issue> activeIssueList = issueRepository.findAllByProjectId(projectId);
        if (activeIssueList.isEmpty()) {
            throw new NoSuchElementException("active한 issue 없음");
        }
        int activeIssueListLength = activeIssueList.stream().toArray().length;
        System.out.println("activeIssueListLength = " + activeIssueListLength);

        // then
        assertNotEquals(notActiveIssueListLength, activeIssueListLength);

//        assertThat(activeIssueListLength, greaterThan(notActiveIssueListLength));
//        assertThat(notActiveIssueListLength, greaterThan(activeIssueListLength));

    }

    @Test
    @AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
    void 이슈_생성() {

        Long newIssueId = 99L;
        Issue newIssue = new Issue().builder()
                .id(newIssueId)
                .title("테스트코드")
                .isActive(true)
                .build();
        issueRepository.save(newIssue);

        Optional<Issue> issueById = issueRepository.findById(99L);
        System.out.println("issueById.get().getTitle() = " + issueById.get().getTitle());

//        assertEquals(issueById.get().getId(), newIssueId);

    }

    @Test
    void GPT_POST요청() throws Exception{
        List<Issue> issueListNotFinished = issueRepository.getIssueListNotFinishedOf(1L);
        // ask importance to gpt
        List<GPTQuestionDTO> questionList = GPTQuestionDTO.organizeIssueListIntoQuestion(issueListNotFinished);

        List<GPTQuestionDTO> gptQuestionDTOList = issueService.sendIssueListToGPT(questionList);


    }

}
