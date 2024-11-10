package spotlight.spotlight_ver2.service;

import spotlight.spotlight_ver2.dto.UserProfileDTO;
import spotlight.spotlight_ver2.entity.User;
import spotlight.spotlight_ver2.repository.UserRepository;
import spotlight.spotlight_ver2.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserProfileService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    // 사용자 프로필 조회
    public UserProfileDTO getUserProfile(String token) {
        String username = jwtUtil.extractUsername(token);
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        String school = user.getStudent() != null ? user.getStudent().getSchool() : null;
        String major = user.getStudent() != null ? user.getStudent().getMajor() : null;
        String company = user.getRecruiter() != null ? user.getRecruiter().getCompany() : null;

        return new UserProfileDTO(
                user.getName(),
                user.getUsername(),
                "********",
                user.getProfileImage(),
                school,
                major,
                company
        );
    }

    // 사용자 프로필 수정
    @Transactional
    public UserProfileDTO updateUserProfile(String token, UserProfileDTO userProfileDTO) {
        String username = jwtUtil.extractUsername(token);
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        user.setName(userProfileDTO.getName());
        user.setProfileImage(userProfileDTO.getProfileImageUrl());

        if (user.getStudent() != null) {
            user.getStudent().setSchool(userProfileDTO.getSchool());
            user.getStudent().setMajor(userProfileDTO.getMajor());
        }

        if (user.getRecruiter() != null) {
            user.getRecruiter().setCompany(userProfileDTO.getCompany());
        }

        String school = user.getStudent() != null ? user.getStudent().getSchool() : null;
        String major = user.getStudent() != null ? user.getStudent().getMajor() : null;
        String company = user.getRecruiter() != null ? user.getRecruiter().getCompany() : null;

        return new UserProfileDTO(
                user.getName(),
                user.getUsername(),
                "********", // 비밀번호는 표시하지 않음
                user.getProfileImage(),
                school,
                major,
                company
        );
    }
}