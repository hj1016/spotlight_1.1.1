package spotlight.spotlight_ver2.security;

import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtil {
    private final Logger logger = LoggerFactory.getLogger(JwtUtil.class);
    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.access.expiration}")
    private long accessExpirationTime;

    @Value("${jwt.refresh.expiration}")
    private long refreshExpirationTime;

    // Access Token 생성
    public String generateAccessToken(String username) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, username, accessExpirationTime);
    }

    // Refresh Token 생성
    public String generateRefreshToken(String username) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, username, refreshExpirationTime);
    }

    // Access Token 갱신
    public String refreshAccessToken(String refreshToken) {
        if (!isValidRefreshToken(refreshToken)) {
            throw new IllegalArgumentException("유효하지 않은 리프레시 토큰입니다. 다시 로그인 해주세요.");
        }

        String username = extractUsername(refreshToken);
        return generateAccessToken(username);
    }

    private String createToken(Map<String, Object> claims, String subject, long expirationTime) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expirationTime);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, secretKey)
                .compact();
    }

    public String extractUsername(String token) {
        try {
            if (token.startsWith("Bearer ")) {
                token = token.substring(7);
            }
            return extractAllClaims(token).getSubject();
        } catch (Exception e) {
            logger.error("Error extracting username: {}", e.getMessage());
            throw new IllegalArgumentException("토큰에서 사용자명을 추출할 수 없습니다.");
        }
    }

    private Claims extractAllClaims(String token) {
        try {
            return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
        } catch (ExpiredJwtException e) {
            logger.error("토큰이 만료되었습니다.");
            throw new IllegalArgumentException("토큰이 만료되었습니다.");
        } catch (UnsupportedJwtException e) {
            logger.error("지원되지 않는 JWT 형식입니다.");
            throw new IllegalArgumentException("유효하지 않은 토큰입니다.");
        } catch (MalformedJwtException e) {
            logger.error("손상된 JWT 형식입니다.");
            throw new IllegalArgumentException("유효하지 않은 토큰입니다.");
        } catch (SignatureException e) {
            logger.error("JWT 서명 검증 실패.");
            throw new IllegalArgumentException("유효하지 않은 토큰입니다.");
        } catch (IllegalArgumentException e) {
            logger.error("토큰이 빈 값입니다.");
            throw new IllegalArgumentException("유효하지 않은 토큰입니다.");
        }
    }

    public boolean isTokenExpired(String token) {
        return extractAllClaims(token).getExpiration().before(new Date());
    }

    public boolean validateToken(String token, CustomUserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    public boolean isValidRefreshToken(String refreshToken) {
        try {
            return !isTokenExpired(refreshToken);
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}