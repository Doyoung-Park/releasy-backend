package kakao99.backend.oauth;


import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import kakao99.backend.entity.Member;
import kakao99.backend.jwt.TokenProvider;
import kakao99.backend.member.dto.Auth2UserInfoDTO;
import kakao99.backend.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Optional;


@Slf4j
@RequiredArgsConstructor
@Component
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler  {

    private final TokenProvider tokenProvider;
    private final MemberRepository memberRepository;
    private final ObjectMapper objectMapper;

    private String oAuthEmail;
    private String oAuthUserName;
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        DefaultOAuth2User user = (DefaultOAuth2User) authentication.getPrincipal();

        String s = objectMapper.writeValueAsString(user);
        log.info("user={}",s);

        OAuth2AuthenticationToken oauth2Token = (OAuth2AuthenticationToken) authentication;


        String provider = oauth2Token.getAuthorizedClientRegistrationId();

        switch (provider) {
            case "google":
            case "naver":
                oAuthEmail = (String) user.getAttributes().get("email");
                oAuthUserName = (String) user.getAttributes().get("name");
                break;
            case "kakao":
                Map<String, Object> kakaoAccount = (Map<String, Object>) user.getAttributes().get("kakao_account");
                Map<String, Object> kakaoProfile = (Map<String, Object>) kakaoAccount.get("profile");

                String kprofile = objectMapper.writeValueAsString(kakaoProfile);
                String kAccount = objectMapper.writeValueAsString(kakaoAccount);
                log.info(kprofile);
                log.info(kAccount);
                oAuthEmail = (String) kakaoAccount.get("email");
                oAuthUserName = (String) kakaoProfile.get("nickname");
                break;
            default:
                break;
        }


        Optional<Member> findMemberByEmail = memberRepository.findByEmail(oAuthEmail);

        if (findMemberByEmail.isEmpty()) {
            //Member member = saveOrUpdate(oAuthEmail,oAuthUserName, provider);
            //String token = tokenProvider.createAccessToken(member);

            response.setStatus(200);
            log.info("로그인 이름={}", oAuthUserName);
            log.info("로그인 이메일={}", oAuthEmail);

            Auth2UserInfoDTO userInfoDTO = new Auth2UserInfoDTO(oAuthUserName, oAuthEmail);

            String redirectUrl = "http://localhost:3000/authentication/sign-up";
            redirectUrl += "?provider=" + URLEncoder.encode(provider, StandardCharsets.UTF_8);
            redirectUrl += "&email=" + URLEncoder.encode(oAuthEmail, StandardCharsets.UTF_8);
            redirectUrl += "&username=" + URLEncoder.encode(oAuthUserName, StandardCharsets.UTF_8);

            response.sendRedirect(redirectUrl);
        } else {
            Member member = saveOrUpdate(oAuthEmail,oAuthUserName, provider);
            String token = tokenProvider.createAccessToken(member);
            response.sendRedirect("http://localhost:3000/social-login?token=" + token);
        }


    }

    private Member saveOrUpdate(String email, String name, String provider) {

        Member member = memberRepository.findByEmail(email)
                .map(entity -> entity.updateOAuth(email, name))
                .orElse(toEntity(email, name, provider));

        return memberRepository.save(member);
    }

    Member toEntity(String email, String name, String provider) {
        Member member = Member.builder()
                .username(name)
                .email(email)
                .provider(provider)
                .build();

        return member;
    }
}
