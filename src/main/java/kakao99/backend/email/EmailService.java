package kakao99.backend.email;

import javax.mail.internet.MimeMessage;

public interface EmailService {

    String sendSimpleMessage(String email, String projectName)throws Exception;
}
