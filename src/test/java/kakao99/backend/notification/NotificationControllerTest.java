package kakao99.backend.notification;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import kakao99.backend.issue.controller.IssueForm;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class NotificationControllerTest {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @DisplayName("Notification 조회 테스트")
    @Test
    public void getNotificationTest() throws Exception {
        final Long projectId = 1L;
        final String url = (String) "/api/project/"+ projectId +"/notification";
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.get(url));
        result.andExpect(status().isOk());
        result.andDo(MockMvcResultHandlers.print());
    }
}
