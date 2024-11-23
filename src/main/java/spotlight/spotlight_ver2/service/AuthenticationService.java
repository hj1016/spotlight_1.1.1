package spotlight.spotlight_ver2.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spotlight.spotlight_ver2.entity.Recruiter;
import spotlight.spotlight_ver2.entity.Student;
import spotlight.spotlight_ver2.entity.User;
import spotlight.spotlight_ver2.repository.RecruiterRepository;
import spotlight.spotlight_ver2.repository.StudentRepository;
import spotlight.spotlight_ver2.repository.UserRepository;
import spotlight.spotlight_ver2.request.EmailSendingRequest;

import java.util.Optional;
import java.security.SecureRandom;
import org.springframework.security.crypto.password.PasswordEncoder;
import spotlight.spotlight_ver2.security.JwtUtil;

@Service
public class AuthenticationService {
    private final UserRepository userRepository;
    private final JavaMailSender mailSender;
    private final RecruiterRepository recruiterRepository;
    private final StudentRepository studentRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtTokenProvider;
    private final Logger logger = LoggerFactory.getLogger(AuthenticationService.class);

    @Autowired
    public AuthenticationService(UserRepository userRepository, JavaMailSender mailSender,
                                 RecruiterRepository recruiterRepository, StudentRepository studentRepository,
                                 PasswordEncoder passwordEncoder, JwtUtil jwtTokenProvider) {
        this.userRepository = userRepository;
        this.mailSender = mailSender;
        this.recruiterRepository = recruiterRepository;
        this.studentRepository = studentRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Transactional
    public String authenticateUser(String username, String password) {
        Optional<User> userOptional = userRepository.findByUsername(username);
        if (userOptional.isPresent() && passwordEncoder.matches(password, userOptional.get().getPassword())) {
            User user = userOptional.get();
            String role = user.getRole().toString();
            String token = jwtTokenProvider.generateAccessToken(user.getUsername(), role);
            return token;
        } else {
            throw new IllegalArgumentException("아이디 또는 비밀번호가 잘못되었습니다.");
        }
    }

    @Transactional
    public boolean sendEmailVerificationCode(EmailSendingRequest emailSendingRequest, String role) {
        String email = emailSendingRequest.getEmail();
        String username = emailSendingRequest.getUsername();

        SecureRandom random = new SecureRandom();
        int code = random.nextInt(900000) + 100000;
        String codeStr = String.valueOf(code);

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("mailsending@gmail.com");
        message.setTo(email);
        message.setSubject("스포트라이트 메일 인증 코드입니다");
        message.setText("스포트라이트에서 보낸 인증 코드 " + codeStr);

        try {
            Optional<User> userOptional = userRepository.findByUsername(emailSendingRequest.getUsername());
            if (userOptional.isPresent()) {
                mailSender.send(message);
                User user = userOptional.get();
                user.setEmailCode(codeStr);
                user.setEmailVerified(false);
                userRepository.save(user);
                logger.info("Email code set for user: {}", user.getId());
                return true;
            } else {
                logger.info("User not found for username: {}", username);
                return false;
            }
        } catch (MailException e) {
            logger.error("메일 전송 오류", e);
            return false;
        }
    }

    @Transactional
    public boolean verifyEmailCode(EmailSendingRequest emailSendingRequest, String role) {
        String username = emailSendingRequest.getUsername();
        String inputCode = emailSendingRequest.getEmailCode();

        Optional<User> userOptional = userRepository.findByUsername(username);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (inputCode.equals(user.getEmailCode())) {
                long id = user.getId();
                if ("STUDENT".equals(role)) {
                    Student student = studentRepository.findById(id).orElse(null);
                    if (student != null) {
                        student.setSchool(emailSendingRequest.getSchool());
                        student.setMajor(emailSendingRequest.getMajor());
                        student.getUser().setEmailVerified(true);
                        studentRepository.save(student);
                    }
                } else if ("RECRUITER".equals(role)) {
                    Recruiter recruiter = recruiterRepository.findById(id).orElse(null);
                    if (recruiter != null) {
                        recruiter.setCompany(emailSendingRequest.getCompany());
                        recruiter.getUser().setEmailVerified(true);
                        recruiterRepository.save(recruiter);
                    }
                }
                return true;
            }
        }
        return false;
    }
}