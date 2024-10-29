package spotlight.spotlight_ver2.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ChatbotService {

    private final RestTemplate restTemplate;

    // 환경 변수에서 비밀 키 가져오기
    @Value("${openai.secret-key}")
    private String apiKey;

    // application.properties에서 모델 설정
    @Value("${openai.model}")
    private String model;

    private static final String API_URL = "https://api.openai.com/v1/chat/completions";

    public ChatbotService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String getChatGPTResponse(String userInput) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + apiKey);
        headers.set("Content-Type", "application/json");

        // ChatGPT API 요청 본문 구성
        String requestBody = "{ \"model\": \"" + model + "\", \"messages\": [{\"role\": \"user\", \"content\": \"" + userInput + "\"}]}";
        HttpEntity<String> request = new HttpEntity<>(requestBody, headers);

        // API 호출 및 응답 받기
        ResponseEntity<String> response = restTemplate.exchange(API_URL, HttpMethod.POST, request, String.class);
        return response.getBody();
    }
}
