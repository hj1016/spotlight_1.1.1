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
        String jwtToken = token.substring(7);
        UserProfileDTO userProfile = userProfileService.getUserProfile(jwtToken);
        return ResponseEntity.ok(userProfile);
    }

    // 사용자 프로필 수정
    @PutMapping
    public ResponseEntity<UserProfileDTO> updateUserProfile(@RequestHeader("Authorization") String token,
                                                            @RequestBody UserProfileDTO userProfileDTO) {
        String jwtToken = token.substring(7);
        UserProfileDTO updatedProfile = userProfileService.updateUserProfile(jwtToken, userProfileDTO);
        return ResponseEntity.ok(updatedProfile);
    }
}