package spotlight.spotlight_ver2.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;
import spotlight.spotlight_ver2.dto.UserProfileDTO;
import spotlight.spotlight_ver2.entity.User;
import spotlight.spotlight_ver2.request.UploadPortfolioRequest;
import spotlight.spotlight_ver2.request.UserProfileUpdateRequest;
import spotlight.spotlight_ver2.response.UploadPortfolioResponse;
import spotlight.spotlight_ver2.response.UserProfileUpdateResponse;
import spotlight.spotlight_ver2.service.JwtService;
import spotlight.spotlight_ver2.service.UserProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import spotlight.spotlight_ver2.service.UserService;

import java.util.Map;

@RestController
@RequestMapping("/api/user/profile")
@Tag(name = "프로필 API", description = "프로필 관련 기능 제공")
public class UserProfileController {

    @Autowired
    private UserProfileService userProfileService;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private UserService userService;
    private UserProfileDTO userProfileDTO;

    // 사용자 프로필 조회
    @GetMapping
    public ResponseEntity<UserProfileDTO> getUserProfile() {
        User user = userService.getCurrentUser();
        UserProfileDTO userProfile = userProfileService.getUserProfile(user);
        return ResponseEntity.ok(userProfile);
    }

    // 사용자 프로필 수정
    @PutMapping
    public ResponseEntity<?> updateImage(@ModelAttribute UserProfileUpdateRequest userProfileUpdateRequest) {
        try {
            User user = userService.getCurrentUser();
            UserProfileUpdateResponse response = userProfileService.updateUserProfile(user, userProfileUpdateRequest);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("서버에서 오류가 발생했습니다. 나중에 다시 시도해 주세요.");
        }
    }
}