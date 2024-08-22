package spotlight.spotlight_ver2.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors().and() // CORS 설정 적용
                .authorizeHttpRequests(authorizeRequests ->
                        authorizeRequests
                                .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-ui.html", "/swagger-resources/**", "/webjars/**").permitAll() // Swagger UI 관련 경로 허용
                                .requestMatchers("/api/exhibitions/**").permitAll() // 전시 정보 API에 대한 접근 허용
                                .requestMatchers("/api/projects/**").permitAll() // 프로젝트 API에 대한 접근 허용
                                .anyRequest().authenticated() // 나머지 요청은 인증 필요
                )
                .csrf().disable(); // 필요시 CSRF 보호 비활성화

        return http.build();
    }
}