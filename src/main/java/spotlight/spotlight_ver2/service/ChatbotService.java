package spotlight.spotlight_ver2.service;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import spotlight.spotlight_ver2.entity.Feed;  // 'Project' 대신 'Feed'로 변경
import spotlight.spotlight_ver2.entity.User;
import spotlight.spotlight_ver2.exception.UnauthorizedException;
import spotlight.spotlight_ver2.repository.CategoryRepository;
import spotlight.spotlight_ver2.request.ChatbotRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ChatbotService {

    private final RestTemplate restTemplate;
    private final String apiKey;
    private final String model;
    private static final String API_URL = "https://api.openai.com/v1/chat/completions";

    private final CategoryRepository categoryRepository;
    private final RecommendationService recommendationService;
    private final FeedService feedService;

    public ChatbotService(RecommendationService recommendationService, RestTemplate restTemplate, CategoryRepository categoryRepository, FeedService feedService) {
        this.recommendationService = recommendationService;
        this.restTemplate = restTemplate;
        this.categoryRepository = categoryRepository;
        this.feedService = feedService;

        Dotenv dotenv = Dotenv.configure().load();
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

    public String getRecommendations(String userField, String keyword, List<String> hashtags, String category) {
        System.out.println("getRecommendations 호출 - 키워드: " + keyword + ", 해시태그: " + hashtags + ", 카테고리: " + category);

        // 최종 추천 결과 저장
        List<Feed> combinedFeeds = new ArrayList<>();
        List<User> students = new ArrayList<>();

        // 1) 키워드와 해시태그 모두 있는 경우
        if (!keyword.isEmpty() && !hashtags.isEmpty()) {
            // 키워드 기반 피드 검색
            List<Feed> keywordFeeds = recommendationService.searchFeedsByKeyword(keyword);
            // 해시태그 기반 피드 검색 (키워드 결과 제외)
            List<Feed> hashtagFeeds = recommendationService.searchFeedsByHashtag(hashtags)
                    .stream()
                    .filter(feed -> !keywordFeeds.contains(feed)) // 중복 제거
                    .collect(Collectors.toList());

            combinedFeeds.addAll(keywordFeeds);
            combinedFeeds.addAll(hashtagFeeds);

            if (!combinedFeeds.isEmpty()) {
                return formatFeedRecommendations(combinedFeeds); // 병합된 피드 추천 결과 반환
            }

            return "키워드와 해시태그에 맞는 프로젝트를 찾을 수 없습니다.";
        }

        // 2) 키워드만 존재하는 경우
        if (!keyword.isEmpty()) {
            // 키워드 기반 인재 추천
            students = recommendationService.recommendUsersByKeyword(keyword);
            if (!students.isEmpty()) {
                return formatStudentRecommendations(students); // 추천된 학생 목록 반환
            }

            // 키워드 기반 피드 추천
            combinedFeeds = recommendationService.searchFeedsByKeyword(keyword);
            if (!combinedFeeds.isEmpty()) {
                return formatFeedRecommendations(combinedFeeds); // 추천된 피드 목록 반환
            }

            return "키워드에 맞는 인재나 프로젝트를 찾을 수 없습니다.";
        }

        // 3) 해시태그만 존재하는 경우
        if (!hashtags.isEmpty()) {
            // 해시태그 기반 피드 추천
            combinedFeeds = recommendationService.searchFeedsByHashtag(hashtags);
            if (!combinedFeeds.isEmpty()) {
                return formatFeedRecommendations(combinedFeeds); // 추천된 피드 목록 반환
            }

            return "해시태그에 맞는 프로젝트를 찾을 수 없습니다.";
        }

        // 4) 카테고리 기반 추천
        if (!category.isEmpty()) {
            // 카테고리 기반 인재 추천
            students = recommendationService.recommendUsersByCategory(category);
            if (!students.isEmpty()) {
                return formatStudentRecommendations(students); // 추천된 학생 목록 반환
            }

            return "카테고리에 맞는 인재를 찾을 수 없습니다.";
        }

        // 5) 키워드, 해시태그, 카테고리 모두 없는 경우
        return "추천을 위해 키워드, 해시태그, 또는 카테고리를 입력해주세요.";
    }

    public String formatStudentRecommendations(List<User> users) {
        StringBuilder response = new StringBuilder("추천 인재 목록:\n");
        users.forEach(user -> response.append("- ").append(user.getName()).append("\n"));
        return response.toString();
    }

    public String formatFeedRecommendations(List<Feed> feeds) {
        StringBuilder response = new StringBuilder("추천 피드 목록:\n");
        feeds.forEach(feed -> response.append("- ").append(feed.getTitle()).append("\n"));
        return response.toString();
    }
}