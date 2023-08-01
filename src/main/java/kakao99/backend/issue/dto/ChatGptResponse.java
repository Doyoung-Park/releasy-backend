package kakao99.backend.issue.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@NoArgsConstructor
//ChatGPT 답변을 담을 DTO
public class ChatGptResponse {
    private String id;
    private String object;
    private Long created;
    private String model;
    private List<Choice> choices;

    @Getter
    @Setter
    public static class Usage {
        @JsonProperty("prompt_tokens")
        private int promptTokens;
        @JsonProperty("completion_tokens")
        private int completionTokens;
        @JsonProperty("total_tokens")
        private int totalTokens;
    }

    @Getter
    @Setter
    public static class Choice {
        private int index;
        private GPTMessage message;
        @JsonProperty("finish_reason")
        private String finishReason;

    }

    @Getter
    @Setter
    public static class GPTMessage {
        private String role;
        private String content;
    }
}

//{id=chatcmpl-7iLLldBTdDUjfcFVnYKzNMC6f4c5L, object=chat.completion, created=1690802857, model=gpt-3.5-turbo-0613, choices=[{index=0, message={role=assistant, content=이슈 정리의 중요도는 80입니다.}, finish_reason=stop}], usage={prompt_tokens=58, completion_tokens=14, total_tokens=72}}