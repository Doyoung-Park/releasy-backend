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



//@DataJpaTest
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
//@ContextConfiguration(classes = BackendApplication.class)
//@Import(TestConfig.class)
@ExtendWith(MockitoExtension.class)
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
                assertEquals(issueList, allIssues);
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





}
