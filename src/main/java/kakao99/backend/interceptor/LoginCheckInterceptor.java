package kakao99.backend.interceptor;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

@RequiredArgsConstructor
public class LoginCheckInterceptor implements HandlerInterceptor {

    private RedisTemplate<String, String> redisTemplate;
    private String secret;

    public LoginCheckInterceptor(RedisTemplate<String, String> redisTemplate, String secret) {
        this.secret = secret;
        this.redisTemplate = redisTemplate;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String bearerToken = request.getHeader("Authorization");
        String accessToken =null;
        System.out.println("accessToken = " + bearerToken);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            accessToken = bearerToken.substring(7);
        }
        if(accessToken == null || accessToken.length() == 0){
            System.out.println("accessToken 없음");
            System.out.println("!! accessToken = " + accessToken);
            return false;
        }else{
            System.out.println("!! accessToken = " + accessToken);
            Claims claims = Jwts
                    .parserBuilder()
                    .setSigningKey(secret)
                    .build()
                    .parseClaimsJws(accessToken)
                    .getBody();

            Long memberId = Long.valueOf(claims.getSubject());

            String memberIdKey = String.valueOf(memberId);
            if (Boolean.TRUE.equals(redisTemplate.hasKey(memberIdKey))) {
                System.out.println("유저 정보 있음");
                System.out.println("memberIdKey = " + memberIdKey);
                return true;

            }else{
                System.out.println("Interceptor2");
                return false;
            }
        }
    }

}