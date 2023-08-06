package kakao99.backend.member.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class RedisController {

    @GetMapping("api/session")
    public String getSessionId(HttpSession session) {
        Object member = session.getAttribute("loggedInUserId");
        System.out.println(member);
        return session.getId();
    }
}
