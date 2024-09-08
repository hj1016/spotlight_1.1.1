package spotlight.spotlight_ver2.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import spotlight.spotlight_ver2.entity.Student;
import spotlight.spotlight_ver2.entity.User;
import spotlight.spotlight_ver2.request.UploadPortfolioRequest;
import spotlight.spotlight_ver2.response.UploadPortfolioResponse;
import spotlight.spotlight_ver2.service.PortfolioService;
import spotlight.spotlight_ver2.service.UserService;

import java.util.Optional;

@Controller
@RequestMapping("/api/user")
public class PortfolioController {

    private final UserService userService;
    private final PortfolioService portfolioService;

    @Autowired
    public PortfolioController(UserService userService, PortfolioService portfolioService) {
        this.userService = userService;
        this.portfolioService = portfolioService;
    }

    // 포트폴리오 업로드 (학생)
    @PostMapping("/portfolio")
    public ResponseEntity uploadPortfolio(@ModelAttribute UploadPortfolioRequest request) {
        try {
            User user = userService.getCurrentUser();
            UploadPortfolioResponse response = portfolioService.uploadPortfolio(user, request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버에서 오류가 발생했습니다. 나중에 다시 시도해 주세요.");
        }
    }

    // 포트폴리오 조회 (리크루터)
    @GetMapping("/{id}/portfolio")
    public ResponseEntity<?> getPortfolio(@PathVariable("id") Integer id) {
        try {
            User user = userService.getUserById(id);  // id로 사용자 조회
            if (user != null) {
                Optional<Student> studentOptional = portfolioService.getStudentById(id);

                if (studentOptional.isPresent()) {
                    Student student = studentOptional.get();
                    String pfPath = student.getPortfolioImage();
                    String[] imageUrls;
                    if (pfPath != null) {
                        imageUrls = pfPath.split(",");
                    } else {
                        imageUrls = new String[0];
                    }
                    ObjectMapper mapper = new ObjectMapper();
                    ObjectNode returnNode = mapper.createObjectNode();
                    returnNode.put("success", true);
                    returnNode.putPOJO("portfolio", imageUrls);
                    return ResponseEntity.ok(returnNode);
                } else {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("학생 사용자를 찾을 수 없습니다.");
                }
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("사용자를 찾을 수 없습니다.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버에서 오류가 발생했습니다. 나중에 다시 시도해주세요.");
        }
    }


}
