package spotlight.spotlight_ver2.service;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import spotlight.spotlight_ver2.entity.*;
import spotlight.spotlight_ver2.exception.NotFoundException;
import spotlight.spotlight_ver2.exception.UnauthorizedException;
import spotlight.spotlight_ver2.repository.CategoryRepository;
import spotlight.spotlight_ver2.request.ChatbotRequest;

import java.util.List;

@Service
public class ChatbotService {

    private final RestTemplate restTemplate;
    private final String apiKey;
    private final String model;
    private static final String API_URL = "https://api.openai.com/v1/chat/completions";

    private final CategoryRepository categoryRepository;
    private final ProjectService projectService;
    private final RecommendationService recommendationService;

    public ChatbotService(ProjectService projectService, RecommendationService recommendationService, RestTemplate restTemplate, CategoryRepository categoryRepository) {
        this.projectService = projectService;
        this.recommendationService = recommendationService;
        this.restTemplate = restTemplate;
        this.categoryRepository = categoryRepository;

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

    public String getRecommendations(String userField, String userInput) {
        if (userField != null && !userField.isEmpty()) {
            // userField (String)를 기반으로 Category 객체 조회
            Category category = categoryRepository.findByName(userField)
                    .orElseThrow(() -> new NotFoundException("해당 이름의 카테고리를 찾을 수 없습니다: " + userField));

            // 해당 카테고리에 맞는 학생 추천
            List<User> users = recommendationService.recommendUsersByCategory(category);

            if (users.isEmpty()) {
                return "해당 카테고리에 맞는 학생을 찾을 수 없습니다.";
            }

            return formatStudentRecommendations(users);

        } else if (userInput != null && !userInput.isEmpty()) {
            // 키워드 기반 학생 추천
            List<User> students = recommendationService.recommendUsersByKeyword(userInput);

            if (!students.isEmpty()) {
                return formatStudentRecommendations(students);
            }

            // 키워드 기반 프로젝트 추천
            List<Project> projects = projectService.searchProjectsByKeyword(userInput);

            if (!projects.isEmpty()) {
                return formatProjectRecommendations(projects);
            }

            // 키워드로 학생과 프로젝트 둘 다 추천 결과가 없는 경우
            return "키워드에 맞는 학생이나 프로젝트를 찾을 수 없습니다.";
        } else {
            return "추천을 위해 카테고리 또는 키워드를 입력해주세요.";
        }
    }

    private String formatStudentRecommendations(List<User> users) {
        StringBuilder response = new StringBuilder("추천 인재 목록:\n");
        users.forEach(user -> response.append("- ").append(user.getName()).append("\n"));
        return response.toString();
    }

    private String formatProjectRecommendations(List<Project> projects) {
        StringBuilder response = new StringBuilder("추천 프로젝트 목록:\n");
        for (Project project : projects) {
            response.append("- ").append(project.getName()).append("\n");
        }
        return response.toString();
    }
}