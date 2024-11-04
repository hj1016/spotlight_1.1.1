package spotlight.spotlight_ver2;

import io.github.cdimascio.dotenv.Dotenv;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class EnvironmentVariableTest {

    @Test
    public void testChatGptApiKeyIsLoaded() {
        Dotenv dotenv = Dotenv.configure().load();
        String apiKey = dotenv.get("CHATGPT_SECRET_KEY");

        // API 키가 null이 아닌지 확인
        assertNotNull(apiKey, "CHATGPT_SECRET_KEY가 로드되지 않았습니다.");
        System.out.println("Loaded ChatGPT API Key: " + apiKey);
    }

    @Test
    public void testCareerApiKeyIsLoaded() {
        Dotenv dotenv = Dotenv.configure().load();
        String careerApiKey = dotenv.get("API_KEY_CAREER");

        // Career API 키가 null이 아닌지 확인
        assertNotNull(careerApiKey, "CAREER_API_KEY가 로드되지 않았습니다.");
        System.out.println("Loaded Career API Key: " + careerApiKey);
    }
}