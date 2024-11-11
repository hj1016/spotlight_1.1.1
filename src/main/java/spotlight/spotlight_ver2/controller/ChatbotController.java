package spotlight.spotlight_ver2.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import spotlight.spotlight_ver2.entity.User;
import spotlight.spotlight_ver2.entity.Category;
import spotlight.spotlight_ver2.repository.UserRepository;
import spotlight.spotlight_ver2.response.ErrorResponse;
import spotlight.spotlight_ver2.service.ChatbotService;
import spotlight.spotlight_ver2.exception.NotFoundException;
import spotlight.spotlight_ver2.service.StudentService;
import spotlight.spotlight_ver2.service.UserService;

@RestController
@RequestMapping("/api/chat")
@Tag(name="챗봇 API", description = "챗봇 서비스를 제공하는 API 입니다.")
public class ChatbotController {

    private final ChatbotService chatbotService;
    private final UserService userService;
    private final StudentService studentService;

    @Autowired
    public ChatbotController(ChatbotService chatbotService, UserService userService, UserRepository userRepository, StudentService studentService) {
        this.chatbotService = chatbotService;
        this.userService = userService;
        this.studentService = studentService;
    }

    @PostMapping("/ask")
    public ResponseEntity<?> askChatbot(@RequestBody String userInput) {
        try {
            User user = userService.getCurrentUser();
            if (user == null) {
                throw new NotFoundException("현재 인증된 사용자가 없습니다.");
            }

            String userRole = String.valueOf(user.getRole()); // NORMAL, STUDENT, RECRUITER 중 하나
            String userField = ""; // 사용자 필드 (STUDENT인 경우 설정됨)

            if ("STUDENT".equals(userRole)) {
                // 학생의 분야를 기반으로 프로젝트 추천
                userField = studentService.determineStudentField(user.getStudent())
                        .map(Category::getName) // Category의 name 필드를 추출
                        .orElse(""); // 없으면 빈 문자열
            }

            // ChatGPT 기반 추천
            String recommendations = chatbotService.getRecommendations(userField, userInput);
            return ResponseEntity.ok(recommendations);

        } catch (NotFoundException ex) {
            return ErrorResponse.toResponseEntity(HttpStatus.NOT_FOUND, ex.getMessage());
        } catch (Exception ex) {
            return ErrorResponse.toResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR, "서버 오류가 발생했습니다.");
        }
    }
}