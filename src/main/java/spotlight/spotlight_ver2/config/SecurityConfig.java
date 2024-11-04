package spotlight.spotlight_ver2.config;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import spotlight.spotlight_ver2.security.ApiKeyAuthFilter;
import spotlight.spotlight_ver2.security.JwtRequestFilter;
import org.springframework.security.config.http.SessionCreationPolicy;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JwtRequestFilter jwtRequestFilter;

    @Bean
    public ApiKeyAuthFilter apiKeyAuthFilter() {
        Dotenv dotenv = Dotenv.configure().load();
        String chatGptApiKey = dotenv.get("CHATGPT_SECRET_KEY");
        String careerNetApiKey = dotenv.get("API_KEY_CAREER");

        // 디버깅: API 키 값 출력
        System.out.println("ChatGPT API Key: " + chatGptApiKey);
        System.out.println("CareerNet API Key: " + careerNetApiKey);

        return new ApiKeyAuthFilter(chatGptApiKey, careerNetApiKey);
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .headers(headers -> headers
                        .frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin) // SAMEORIGIN 허용
                )
                .authorizeHttpRequests(authorizeRequests -> authorizeRequests
                        .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()
                        .requestMatchers("/error").permitAll() // spring security의 계속된 403 error 반환을 막기 위함
                        .requestMatchers("/api/chatgpt/**", "/api/careernet/**").authenticated()
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                );

        // ApiKeyAuthFilter 추가
        http.addFilterBefore(apiKeyAuthFilter(), UsernamePasswordAuthenticationFilter.class);

        // JwtRequestFilter 추가
        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}