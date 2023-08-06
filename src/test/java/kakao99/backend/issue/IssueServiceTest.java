package kakao99.backend.issue;


import kakao99.backend.entity.Issue;
import kakao99.backend.entity.Member;
import kakao99.backend.issue.dto.GPTQuestionDTO;
import kakao99.backend.issue.dto.IssueDTO;
import kakao99.backend.issue.repository.IssueParentChildRepository;
import kakao99.backend.issue.repository.IssueRepository;
import kakao99.backend.issue.repository.IssueRepositoryImpl;
import kakao99.backend.issue.service.IssueService;
import kakao99.backend.member.repository.MemberRepository;
import kakao99.backend.notification.service.NotificationService;
import kakao99.backend.project.service.ProjectService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static reactor.core.publisher.Mono.when;


//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)


@ExtendWith(MockitoExtension.class)
//@DataJpaTest
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
//@ContextConfiguration(classes = BackendApplication.class)
//@Import(TestConfig.class)
//@ExtendWith(MockitoExtension.class)
public class IssueServiceTest {

    @InjectMocks
    IssueService issueService;


//    @MockBean
//    @Autowired
    @Mock
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

    @Nested
    @DisplayName("모든 이슈 조회 테스트")
    class GetIssue {

        private Long projectId;

        @BeforeEach
        void setUp() {
            projectId = 1L;
        }

        @Nested
        @DisplayName("정상 케이스")
        class SuccessCase {

            @DisplayName("모든 이슈 조회")
            @Test
            void getAllIssues() {
                List<Issue> issueList = new ArrayList<>();
                given(issueRepository.findAllByProjectId(any(Long.class))).willReturn(issueList);

                List<IssueDTO> allIssues = issueService.getAllIssues(projectId);
                assertNotNull(allIssues);

            }
        }

        @Nested
        @DisplayName("실패 케이스")
        class FailCase {
            @Test
            @DisplayName("타입 불일치")
            void getAnotherType() {
                Issue issue = new Issue();
                List<Issue> issueList = new ArrayList<>();
                given(issueRepository.findAllByProjectId(any(Long.class))).willReturn(issueList);

                List<IssueDTO> allIssues = issueService.getAllIssues(projectId);
                assertEquals(allIssues, issue);
            }
        }
    }


    @Test
    void 이슈_조회() {

        Long issueId = 99L;

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

//    @Test
//    void 잔디밭_데이터_테스트() {
//        Long memberId = 1L;
//        List<IssueGrassDTO> numberOfDoneIssues = issueRepository.findNumberOfDoneIssues(memberId);
//
//        System.out.println("numberOfDoneIssues.stream().count() = " + numberOfDoneIssues.stream().count());
//        for (IssueGrassDTO grassData : numberOfDoneIssues) {
//            System.out.println("grassData.getCount() = " + grassData.getCount());
//            System.out.println("grassData.getUpdatedAt() = " + grassData.getUpdatedAt());
//            System.out.println("====");
//
//        }
//        System.out.println("numberOfDoneIssues.get(0) = " + numberOfDoneIssues.get(0).toString());
//    }
@Test
@DisplayName("프로젝트의 max IssueNum")
public void getMaxIssueNum() {
    Long projectId = 1L;
    Long maxIssueNum = issueRepository.findMaxIssueNum(projectId).get();
    System.out.println("maxIssueNum = " + maxIssueNum);

}
}
