package spotlight.spotlight_ver2;

import io.github.cdimascio.dotenv.Dotenv;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class EnvironmentVariableTest {

    private static Dotenv dotenv;

    @BeforeAll
    public static void setUp() {
        // Dotenv로 .env 파일 로드
        dotenv = Dotenv.configure().load();
    }

    @Test
    public void testChatGptApiKeyIsLoaded() {
        String apiKey = dotenv.get("CHATGPT_SECRET_KEY");

        // API 키가 null이 아닌지 확인
        assertNotNull(apiKey, "CHATGPT_SECRET_KEY가 로드되지 않았습니다.");
        System.out.println("Loaded ChatGPT API Key: " + apiKey);
    }

    @Test
    public void testCareerApiKeyIsLoaded() {
        String careerApiKey = dotenv.get("API_KEY_CAREER");

        // Career API 키가 null이 아닌지 확인
        assertNotNull(careerApiKey, "API_KEY_CAREER가 로드되지 않았습니다.");
        System.out.println("Loaded Career API Key: " + careerApiKey);
    }
}