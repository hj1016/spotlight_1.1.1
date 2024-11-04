package spotlight.spotlight_ver2.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.annotation.Order;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

import java.io.IOException;

@Component
@Order(1)
public class ApiKeyAuthFilter extends OncePerRequestFilter {
    private final String chatGptApiKey;
    private final String careerNetApiKey;

    // 생성자에서 ChatGPT 및 CareerNet API 키를 전달받음
    public ApiKeyAuthFilter(String chatGptApiKey, String careerNetApiKey) {
        this.chatGptApiKey = chatGptApiKey;
        this.careerNetApiKey = careerNetApiKey;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String requestURI = request.getRequestURI();
        String apiKey = request.getHeader("X-API-KEY");

        System.out.println("Request URI: " + requestURI);
        System.out.println("Header API Key: " + apiKey);
        System.out.println("ChatGPT API Key: " + chatGptApiKey);
        System.out.println("CareerNet API Key: " + careerNetApiKey);

        // Swagger와 /error 경로 우회
        if (requestURI.startsWith("/swagger-ui") || requestURI.startsWith("/v3/api-docs") || requestURI.equals("/error")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 모든 /api 경로에 대해 API 키 검증
        if (requestURI.startsWith("/api") && (chatGptApiKey.equals(apiKey) || careerNetApiKey.equals(apiKey))) {
            Authentication auth = new PreAuthenticatedAuthenticationToken(apiKey, null, null);
            SecurityContextHolder.getContext().setAuthentication(auth);
            System.out.println("Authentication set for API endpoint: " + requestURI);
        } else {
            System.out.println("Invalid API Key for URI: " + requestURI);
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid API Key");
            return;
        }

        filterChain.doFilter(request, response);
    }
}