package kakao99.backend.email;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.mail.Message;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService{

    private final JavaMailSender javaMailSender;

    public MimeMessage createInviteProjectMessage(String toEmail, String projectName) throws Exception {
        MimeMessage message = javaMailSender.createMimeMessage();

        message.addRecipients(Message.RecipientType.TO, toEmail); // 받는 대상
        message.setSubject("[Releasy] 프로젝트 초대"); // 제목

        String msgg = "<html>" +
                "<head><style>" +
                "    body {font-family: Arial, sans-serif;}" +
                "    .container {max-width: 600px; margin: 0 auto; padding: 20px;}" +
                "    .header {background-color: #f5f5f5; padding: 10px; text-align: center;}" +
                "    .content {background-color: #ffffff; padding: 20px;}" +
                "</style></head>" +
                "<body>" +
                "    <div class='container'>" +
                "        <div class='header'>" +
                "            <h1>안녕하세요, Releasy입니다.</h1>" +
                "        </div>" +
                "        <div class='content'>" +
                "            <p>" + projectName + "에 초대되었습니다.</p>" +
                "            <p>감사합니다!</p>" +
                "        </div>" +
                "    </div>" +
                "</body>" +
                "</html>";

        message.setContent(msgg, "text/html; charset=utf-8"); // 내용을 HTML 형식으로 설정
        message.setFrom(new InternetAddress("gcukakao99@gmail.com", "Releasy")); // 보내는 사람

        return message;
    }

    @Override
    public String sendSimpleMessage(String email, String projectName)throws Exception {
        // TODO Auto-generated method stub
        MimeMessage message = createInviteProjectMessage(email, projectName);
        try{//예외처리
            javaMailSender.send(message);
        }catch(MailException es){
            es.printStackTrace();
            throw new IllegalArgumentException();
        }
        return email;
    }

}
