package spotlight.spotlight_ver2.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import spotlight.spotlight_ver2.dto.PasswordValidationResponseDTO;
import spotlight.spotlight_ver2.dto.UserRegistrationDTO;
import spotlight.spotlight_ver2.entity.Recruiter;
import spotlight.spotlight_ver2.entity.Student;
import spotlight.spotlight_ver2.entity.User;
import spotlight.spotlight_ver2.enums.Role;
import spotlight.spotlight_ver2.repository.RecruiterRepository;
import spotlight.spotlight_ver2.repository.StudentRepository;
import spotlight.spotlight_ver2.repository.UserRepository;
import spotlight.spotlight_ver2.request.CertificateRequest;
import spotlight.spotlight_ver2.response.CertificateResponse;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RecruiterRepository recruiterRepository;
    private final StudentRepository studentRepository;
    private final UploadImageService uploadImageService;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public UserService(UserRepository userRepository, RecruiterRepository recruiterRepository, StudentRepository studentRepository, UploadImageService uploadImageService) {
        this.userRepository = userRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
        this.recruiterRepository = recruiterRepository;
        this.studentRepository = studentRepository;
        this.uploadImageService = uploadImageService;
    }

    // 이메일 중복 검사
    public boolean existEmail(String email) {
        Optional<User> userOptional = this.userRepository.findByEmail(email);
        return userOptional.isPresent();
    }

    // 아이디(username) 중복 검사
    public boolean existUsername(String username) {
        Optional<User> userOptional = this.userRepository.findByUsername(username);
        return userOptional.isPresent();
    }

    // 비밀번호 유효성 검사
    public PasswordValidationResponseDTO validatePassword(String password) {
        if (password == null || password.isEmpty()) {
            return new PasswordValidationResponseDTO(false, "비밀번호는 공백일 수 없습니다.");
        }
        if (!password.matches("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$")) {
            return new PasswordValidationResponseDTO(false, "비밀번호는 최소 8자 이상이어야 하며, 영문자와 숫자를 포함해야 합니다.");
        }
        return new PasswordValidationResponseDTO(true, "유효한 비밀번호입니다.");
    }

    // 회원가입 새로운 유저 등록
    @Transactional
    public User registerNewUser(UserRegistrationDTO userRegistrationDTO) {
        if (userRegistrationDTO.getEmail() == null || userRegistrationDTO.getEmail().isEmpty()) {
            throw new IllegalArgumentException("이메일은 공백일 수 없습니다.");
        }

        if (existEmail(userRegistrationDTO.getEmail())) {
            throw new IllegalArgumentException("이미 등록된 이메일입니다. 다른 이메일을 사용해 주세요.");
        }

        if (userRegistrationDTO.getUsername() == null || userRegistrationDTO.getUsername().isEmpty()) {
            throw new IllegalArgumentException("아이디는 공백일 수 없습니다.");
        }

        if (existUsername(userRegistrationDTO.getUsername())) {
            throw new IllegalArgumentException("이미 등록된 아이디입니다. 다른 아이디를 사용해 주세요.");
        }

        if (!validatePassword(userRegistrationDTO.getPassword()).isValid()) {
            throw new IllegalArgumentException("비밀번호는 최소 8자 이상이어야 하며 영문과 숫자의 조합이어야 합니다.");
        }

        if (userRegistrationDTO.getName() == null || userRegistrationDTO.getName().isEmpty()) {
            throw new IllegalArgumentException("이름은 공백일 수 없습니다.");
        }

        if (userRegistrationDTO.getRole() == null) {
            userRegistrationDTO.setRole(Role.NORMAL);
        }

        Role role = userRegistrationDTO.getRole();

        User newUser = new User();
        newUser.setEmail(userRegistrationDTO.getEmail());
        newUser.setUsername(userRegistrationDTO.getUsername());
        newUser.setPassword(passwordEncoder.encode(userRegistrationDTO.getPassword()));
        newUser.setName(userRegistrationDTO.getName());
        newUser.setRole(role);
        newUser.setCreatedDate(LocalDateTime.now());
        newUser.setUpdatedDate(LocalDateTime.now());

        try {
            User resultUser = this.userRepository.save(newUser);
            if (resultUser.getId() == null) {
                throw new RuntimeException("사용자 등록 실패: ID가 null입니다.");
            }

            if (role == Role.STUDENT) { // role.equals(Role.STUDENT)도 enum에선 같은 동작을 하지만 참조 비교를 하는 ==이 성능이 더 좋음
                Student student = new Student();
                student.setUser(resultUser);
                student.setSchool(userRegistrationDTO.getSchool());
                student.setMajor(userRegistrationDTO.getMajor());
                student.setStudentCertificate(userRegistrationDTO.getStudentCertification());
                try {
                    studentRepository.save(student);
                } catch (Exception e) {
                    throw new RuntimeException("학생 유저 등록에 실패했습니다.");
                }
            } else if (role == Role.RECRUITER) {
                Recruiter recruiter = new Recruiter();
                recruiter.setUser(resultUser);
                recruiter.setCompany(userRegistrationDTO.getCompany());
                recruiter.setRecruiterCertificate(userRegistrationDTO.getRecruiterCertification());
                try {
                    recruiterRepository.save(recruiter);
                } catch (Exception e) {
                    throw new RuntimeException("리크루터 유저 등록에 실패했습니다.");
                }
            }
            return resultUser;
        } catch (Exception e) {
            throw new RuntimeException("사용자 등록에 실패했습니다.");
        }
    }

    // 학생 재학증명서 업로드
    public CertificateResponse uploadStudentCertificate(User user, CertificateRequest certificateRequest) {
        CertificateResponse certificateResponse = new CertificateResponse();
        String imageUrl;
        MultipartFile certificate = certificateRequest.getCertificate();
        Optional<Student> studentOptional = studentRepository.findById(user.getId());

        if (certificate != null && !certificate.isEmpty() && studentOptional.isPresent()) {
            try {
                imageUrl = uploadImageService.uploadImage(certificate);
                Student student = studentOptional.get();
                student.setStudentCertificate(imageUrl);
                studentRepository.save(student);
                certificateResponse.setCertificate(imageUrl);
                certificateResponse.setSuccess(true);
            } catch (IOException e) {
                throw new RuntimeException("학생 재학증명서 업로드에 실패했습니다.", e);
            }
        } else {
            certificateResponse.setSuccess(false);
            certificateResponse.setMessage("재학증명서가 유효하지 않거나 학생 정보가 없습니다.");  // setMessage 추가
        }

        return certificateResponse;
    }

    // 리크루터 재직증명서 업로드
    public CertificateResponse uploadRecruiterCertificate(User user, CertificateRequest certificateRequest) {
        CertificateResponse certificateResponse = new CertificateResponse();
        String imageUrl;
        MultipartFile certificate = certificateRequest.getCertificate();
        Optional<Recruiter> recruiterOptional = recruiterRepository.findById(user.getId());

        if (certificate != null && !certificate.isEmpty() && recruiterOptional.isPresent()) {
            try {
                imageUrl = uploadImageService.uploadImage(certificate);
                Recruiter recruiter = recruiterOptional.get();
                recruiter.setRecruiterCertificate(imageUrl);
                recruiterRepository.save(recruiter);
                certificateResponse.setCertificate(imageUrl);
                certificateResponse.setSuccess(true);
            } catch (IOException e) {
                throw new RuntimeException("리크루터 인증서 업로드에 실패했습니다.", e);
            }
        } else {
            certificateResponse.setSuccess(false);
            certificateResponse.setMessage("인증서가 유효하지 않거나 리크루터 정보가 없습니다.");
        }

        return certificateResponse;
    }

    public User getUserById(long id) {
        return userRepository.findById(id).orElse(null);
    }

    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && !(authentication instanceof AnonymousAuthenticationToken)) {
            String username = authentication.getName();
            return userRepository.findByUsername(username).orElse(null);
        }
        return null;
    }
}