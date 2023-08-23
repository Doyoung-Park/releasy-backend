package kakao99.backend.config;

import kakao99.backend.interceptor.LoginCheckInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

    private final RedisTemplate<String, String> redisTemplate;

    @Value("${jwt.secret}")
    private String secret;

    public InterceptorConfig(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginCheckInterceptor(redisTemplate, secret)) //인터셉터 등록. 여기서 LoginCheckInterceptor()은 내가 구현한 클래스 이름이다.
                .order(1) //낮을 수록 먼저 호출
                .addPathPatterns("/**") //인터셉터를 적용할 url 패턴
                .excludePathPatterns("/api/member/signup/**", "/api/member/rejoin/**", "/authentication/**"); //인터셉터에서 제외할 패턴 지정
    }
}