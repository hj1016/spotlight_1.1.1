package spotlight.spotlight_ver2.service;

import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.util.Base64;

@Service
public class JwtService {
    public String extractRole(String token) {
        // JWT 토큰에서 역할(role) 정보를 추출하는 로직
        String[] splitToken = token.split("\\.");
        String payload = new String(Base64.getDecoder().decode(splitToken[1]));
        try {
            JSONObject jsonObject = new JSONObject(payload);
            return jsonObject.getString("role");
        } catch (Exception e) {
            throw new RuntimeException("토큰에서 ROLE을 추출하는 데 실패했습니다.", e);
        }
    }
}
