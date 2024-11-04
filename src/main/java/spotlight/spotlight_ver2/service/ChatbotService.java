package spotlight.spotlight_ver2.service;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import spotlight.spotlight_ver2.exception.UnauthorizedException;
import spotlight.spotlight_ver2.request.ChatbotRequest;

import java.util.List;

@Service
public class ChatbotService {

    private final RestTemplate restTemplate;
    private final String apiKey;
    private final String model;
    private static final String API_URL = "https://api.openai.com/v1/chat/completions";

    public ChatbotService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;

        // dotenv로 .env 파일에서 변수 읽기
        Dotenv dotenv = Dotenv.load();
        this.apiKey = dotenv.get("CHATGPT_SECRET_KEY");
        this.model = dotenv.get("OPENAI_MODEL");
    }

    public String getChatGPTResponse(String userInput) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + apiKey);

        ChatbotRequest.Message message = new ChatbotRequest.Message("user", userInput);
        ChatbotRequest chatbotRequest = new ChatbotRequest(model, List.of(message));

        HttpEntity<ChatbotRequest> request = new HttpEntity<>(chatbotRequest, headers);

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(API_URL, request, String.class);
            System.out.println("Response: " + response.getBody());
            return response.getBody();
        } catch (HttpClientErrorException e) {
            System.out.println("Error Response: " + e.getResponseBodyAsString());
            throw new UnauthorizedException("Unauthorized: " + e.getMessage());
        }
    }
}