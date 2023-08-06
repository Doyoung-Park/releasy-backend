package kakao99.backend.issue.dto;

import kakao99.backend.entity.Issue;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

@Builder
@Getter
@Setter
public class GPTQuestionDTO {
    private Long id;
    private String question;
    private int importance;


    public static List<GPTQuestionDTO> organizeIssueListIntoQuestion(List<Issue> issueListNotFinished) {
        List<GPTQuestionDTO> questionList = issueListNotFinished.stream().map(issue ->
                GPTQuestionDTO.builder()
                        .id(issue.getId())
                        .question(issue.getTitle())
                        .importance(0)
                        .build()
        ).collect(Collectors.toList());
        return questionList;
    }
}
