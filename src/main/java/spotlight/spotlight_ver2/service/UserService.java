package spotlight.spotlight_ver2.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import spotlight.spotlight_ver2.dto.PasswordValidationResponseDTO;
import spotlight.spotlight_ver2.dto.UserRegistrationDTO;
import spotlight.spotlight_ver2.entity.Recruiter;
import spotlight.spotlight_ver2.entity.Student;
import spotlight.spotlight_ver2.entity.User;
import spotlight.spotlight_ver2.enums.Role;
import spotlight.spotlight_ver2.repository.RecruiterRepository;
import spotlight.spotlight_ver2.repository.StudentRepository;
import spotlight.spotlight_ver2.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RecruiterRepository recruiterRepository;
    private final StudentRepository studentRepository;

    @Autowired
    public UserService(UserRepository userRepository, RecruiterRepository recruiterRepository, StudentRepository studentRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
        this.recruiterRepository = recruiterRepository;
        this.studentRepository = studentRepository;
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

            if (role == Role.STUDENT) { // role.equals(Role.STUDENT)도 enum에선 같은 동작을 하지만 참조 비교를 하는 ==이 성능이 더 좋음
                Student student = new Student();
                student.setUser(resultUser);
                student.setSchool(userRegistrationDTO.getSchool());
                student.setMajor(userRegistrationDTO.getMajor());
                try {
                    studentRepository.save(student);
                } catch (Exception e) {
                    throw new RuntimeException("학생 유저 등록에 실패했습니다.");
                }
            } else if (role == Role.RECRUITER) {
                Recruiter recruiter = new Recruiter();
                recruiter.setUser(resultUser);
                recruiter.setCompany(userRegistrationDTO.getCompany());
                recruiter.setCertification(userRegistrationDTO.getCertification());
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
}