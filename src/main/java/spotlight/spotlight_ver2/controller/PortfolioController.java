package spotlight.spotlight_ver2.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import spotlight.spotlight_ver2.entity.Student;
import spotlight.spotlight_ver2.entity.User;
import spotlight.spotlight_ver2.request.UploadPortfolioRequest;
import spotlight.spotlight_ver2.response.UploadPortfolioResponse;
import spotlight.spotlight_ver2.service.PortfolioService;
import spotlight.spotlight_ver2.service.UserService;

import java.util.Optional;

@Controller
@RequestMapping("/api/user")
@Tag(name = "포트폴리오 API", description = "포트폴리오 업로드 및 조회 기능 제공")
public class PortfolioController {
    private final Logger logger = LoggerFactory.getLogger(PortfolioController.class);
    private final UserService userService;
    private final PortfolioService portfolioService;

    @Autowired
    public PortfolioController(UserService userService, PortfolioService portfolioService) {
        this.userService = userService;
        this.portfolioService = portfolioService;
    }

    // 포트폴리오 업로드 (학생)
    @Operation(summary = "포트폴리오 업로드", description = "학생 사용자가 포트폴리오를 업로드합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "포트폴리오 업로드 성공"),
            @ApiResponse(responseCode = "500", description = "서버 오류로 업로드 실패")
    })
    @PostMapping("/portfolio")
    public ResponseEntity<?> uploadPortfolio(@ModelAttribute UploadPortfolioRequest uploadRequest) {
        try {
            User user = userService.getCurrentUser();
            UploadPortfolioResponse response = portfolioService.uploadPortfolio(user, uploadRequest);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("서버에서 오류가 발생했습니다. 나중에 다시 시도해 주세요.");
        }
    }

    // 포트폴리오 조회 (리크루터)
    @Operation(summary = "포트폴리오 조회", description = "리크루터가 특정 학생의 포트폴리오를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "포트폴리오 조회 성공"),
            @ApiResponse(responseCode = "404", description = "사용자 또는 학생 사용자를 찾을 수 없음"),
            @ApiResponse(responseCode = "500", description = "서버 오류로 조회 실패")
    })
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