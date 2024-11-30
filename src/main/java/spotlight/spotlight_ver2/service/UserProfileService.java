package spotlight.spotlight_ver2.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;
import spotlight.spotlight_ver2.dto.UserProfileDTO;
import spotlight.spotlight_ver2.entity.User;
import spotlight.spotlight_ver2.repository.UserRepository;
import spotlight.spotlight_ver2.request.UserProfileUpdateRequest;
import spotlight.spotlight_ver2.response.UserProfileUpdateResponse;
import spotlight.spotlight_ver2.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;

@Service
public class UserProfileService {

    @Autowired
    private UploadImageService uploadImageService;

    @Autowired
    private JwtUtil jwtUtil;
    private static final Logger logger = LoggerFactory.getLogger(UserProfileService.class);

    // 사용자 프로필 조회
    public UserProfileDTO getUserProfile(User user) {
        String school = user.getStudent() != null ? user.getStudent().getSchool() : null;
        String major = user.getStudent() != null ? user.getStudent().getMajor() : null;
        String company = user.getRecruiter() != null ? user.getRecruiter().getCompany() : null;
        String role = user.getRole() != null ? user.getRole().name() : "NORMAL";

        return new UserProfileDTO(
                user.getName(),
                user.getUsername(),
                "********",
                user.getProfileImage(),
                school,
                major,
                company,
                role
        );
    }

    // 사용자 프로필 수정
    @Transactional
    public UserProfileUpdateResponse updateUserProfile(User user, UserProfileUpdateRequest userProfileUpdateRequest) {
        UserProfileUpdateResponse response = new UserProfileUpdateResponse();
        String name = userProfileUpdateRequest.getName();
        MultipartFile imageUrl = userProfileUpdateRequest.getProfileImage();
        String returnedImageUrl = "";
        user.setName(name);
        if (!imageUrl.isEmpty()) {
            try {
                returnedImageUrl = uploadImageService.uploadImage(imageUrl);
            } catch (IOException e) {
                logger.error(e.getMessage());
                throw new RuntimeException(e);
            } catch (Exception e) {
                logger.error(e.getMessage());
            }
            user.setProfileImage(returnedImageUrl);
            response.setSuccess(true);
            response.setName(name);
            response.setProfileImageUrl(returnedImageUrl);
            return response;
        }
        response.setSuccess(false);
        return response;
    }

}