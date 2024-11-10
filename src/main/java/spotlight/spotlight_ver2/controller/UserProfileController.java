package spotlight.spotlight_ver2.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import spotlight.spotlight_ver2.dto.UserProfileDTO;
import spotlight.spotlight_ver2.service.UserProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user/profile")
@Tag(name = "프로필 API", description = "프로필 관련 기능 제공")
public class UserProfileController {

    @Autowired
    private UserProfileService userProfileService;

    // 사용자 프로필 조회
    @GetMapping
    public ResponseEntity<UserProfileDTO> getUserProfile(@RequestHeader("Authorization") String token) {
        UserProfileDTO userProfile = userProfileService.getUserProfile(token);
        return ResponseEntity.ok(userProfile);
    }

    // 사용자 프로필 수정
    @PutMapping
    public ResponseEntity<UserProfileDTO> updateUserProfile(@RequestHeader("Authorization") String token,
                                                            @RequestBody UserProfileDTO userProfileDTO) {
        UserProfileDTO updatedProfile = userProfileService.updateUserProfile(token, userProfileDTO);
        return ResponseEntity.ok(updatedProfile);
    }
}