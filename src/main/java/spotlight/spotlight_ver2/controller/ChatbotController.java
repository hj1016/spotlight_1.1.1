package spotlight.spotlight_ver2.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import spotlight.spotlight_ver2.dto.FeedRecommendationDTO;
import spotlight.spotlight_ver2.dto.StudentRecommendationDTO;
import spotlight.spotlight_ver2.entity.User;
import spotlight.spotlight_ver2.entity.Category;
import spotlight.spotlight_ver2.repository.UserRepository;
import spotlight.spotlight_ver2.response.ErrorResponse;
import spotlight.spotlight_ver2.service.*;
import spotlight.spotlight_ver2.exception.NotFoundException;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/chat")
@Tag(name="챗봇 API", description = "챗봇 서비스를 제공하는 API 입니다.")
public class ChatbotController {

    private final ChatbotService chatbotService;
    private final UserService userService;
    private final StudentService studentService;
    private final IntentService intentService;
    private final RecommendationService recommendationService;

    @Autowired
    public ChatbotController(ChatbotService chatbotService, UserService userService, UserRepository userRepository, StudentService studentService, IntentService intentService, RecommendationService recommendationService) {
        this.chatbotService = chatbotService;
        this.userService = userService;
        this.studentService = studentService;
        this.intentService = intentService;
        this.recommendationService = recommendationService;
    }

    @PostMapping("/ask")
    public ResponseEntity<?> askChatbot(@RequestBody String userInput) {
        try {
            User user = userService.getCurrentUser();
            if (user == null) {
                throw new NotFoundException("현재 인증된 사용자가 없습니다.");
            }

            String userRole = String.valueOf(user.getRole());
            String category = "";

            if ("STUDENT".equals(userRole)) {
                category = studentService.findMostRecentFeedCategory(user.getStudent())
                        .map(Category::getName)
                        .orElse("");
            }

            String intent = intentService.determineIntent(userInput);
            String keyword = intentService.extractKeyword(userInput);
            List<String> hashtag = intentService.extractAllHashtags(userInput);
            List<?> recommendations;

            switch (intent) {
                case "keyword_recommendation":
                    recommendations = recommendationService.searchFeedsByKeyword(keyword)
                            .stream()
                            .map(feed -> new FeedRecommendationDTO(feed.getFeedId(), feed.getTitle(), feed.getThumbnailImage()))
                            .collect(Collectors.toList());
                    break;
                case "hashtag_recommendation":
                    recommendations = recommendationService.searchFeedsByHashtag(hashtag)
                            .stream()
                            .map(feed -> new FeedRecommendationDTO(feed.getFeedId(), feed.getTitle(), feed.getThumbnailImage()))
                            .collect(Collectors.toList());
                    break;
                case "category_recommendation":
                    recommendations = recommendationService.recommendUsersByCategory(category)
                            .stream()
                            .map(student -> {
                                String feedCategory = studentService.findMostRecentFeedCategory(student.getStudent())
                                        .map(Category::getName)
                                        .orElse("카테고리 없음");
                                return new StudentRecommendationDTO(student.getUsername(), student.getName(), feedCategory);
                            })
                            .collect(Collectors.toList());
                    break;
                case "keyword_for_student_recommendation":
                    recommendations = recommendationService.recommendUsersByKeyword(keyword)
                            .stream()
                            .map(student -> {
                                String feedCategory = studentService.findMostRecentFeedCategory(student.getStudent())
                                        .map(Category::getName)
                                        .orElse("카테고리 없음");
                                return new StudentRecommendationDTO(student.getUsername(), student.getName(), feedCategory);
                            })
                            .collect(Collectors.toList());
                    break;
                default:
                    recommendations = List.of("추천을 위해 키워드, 해시태그, 또는 카테고리를 입력해주세요.");
            }

            return recommendations.isEmpty()
                    ? ResponseEntity.ok("해당 조건에 맞는 결과를 찾을 수 없습니다.")
                    : ResponseEntity.ok(recommendations);

        } catch (NotFoundException ex) {
            return ErrorResponse.toResponseEntity(HttpStatus.NOT_FOUND, ex.getMessage());
        } catch (Exception ex) {
            return ErrorResponse.toResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR, "서버 오류가 발생했습니다.");
        }
    }

    // 일반적인 불용어 목록 (한국어 기준)
    public static final Set<String> STOP_WORDS = new HashSet<>(Arrays.asList(
            "을", "를", "이", "가", "은", "는", "에", "의", "들", "와", "과", "또는", "그리고", "하지만", "그래서"
    ));
}