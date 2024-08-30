package spotlight.spotlight_ver2.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import spotlight.spotlight_ver2.entity.User;
import spotlight.spotlight_ver2.enums.Role;
import spotlight.spotlight_ver2.repository.RecruiterRepository;
import spotlight.spotlight_ver2.repository.StudentRepository;
import spotlight.spotlight_ver2.repository.UserRepository;
import spotlight.spotlight_ver2.request.UserProfileUpdateRequest;

import java.io.IOException;

@Service
public class UserProfileService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private RecruiterRepository recruiterRepository;

    // 학교, 학과, 회사 필요
    private String school = "";
    private String major = "";
    private String company = "";
    @Autowired
    private UploadImageService uploadImageService;

    // 프로필 정보 불러오기
    public ObjectNode getUserProfile(User user) {
        long id = user.getId();
        Role role = user.getRole();

        // 1. 사용자 프로필을 저장할 객체 생성
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.createObjectNode();

        // 2. 학생 프로필 가져오기
        if (role == Role.STUDENT) {
            this.studentRepository.findById(id).ifPresent(student -> {
                this.school = student.getSchool();
                this.major = student.getMajor();
            });
        }

        // 3. 리크루터 프로필 가져오기
        if (role == Role.RECRUITER) {
            this.recruiterRepository.findById(id).ifPresent(recruiter -> {
                this.company = recruiter.getCompany();
            });
        }

        // 4. 사용자 프로필 노드 리턴
        // 사용자 이름, username, 프로필 이미지
        String name = this.userRepository.findById(id).get().getName();
        String username = this.userRepository.findById(id).get().getUsername();
        String profileImage = this.userRepository.findById(id).get().getProfileImage();

        node.put("name", name);
        node.put("username", username);
        node.put("profileImage", profileImage);

        // 학생, 리크루터 추가 부분
        if (role == Role.STUDENT) {
            node.put("school", school);
            node.put("major", major);
        } else if (role == Role.RECRUITER) {
            node.put("company", company);
        }
        return node;
    }

    // 프로필 업데이트
    public User updateUserProfile(User user, UserProfileUpdateRequest userProfileUpdateRequest) {
        String imageUrl;
        user.setName(userProfileUpdateRequest.getName());
        MultipartFile profileImage = userProfileUpdateRequest.getProfileImage();
        if (profileImage != null && !profileImage.isEmpty()) {
            try {
                imageUrl = uploadImageService.uploadImage(profileImage);
            } catch (IOException e) {
                throw new RuntimeException(e);
            } user.setProfileImage(imageUrl);
        }
        user.setUsername(userProfileUpdateRequest.getName());
        return userRepository.save(user);
    }
}
