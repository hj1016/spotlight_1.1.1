package spotlight.spotlight_ver2.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import spotlight.spotlight_ver2.dto.PasswordValidationDTO;
import spotlight.spotlight_ver2.dto.PasswordValidationResponseDTO;
import spotlight.spotlight_ver2.dto.UserRegistrationDTO;
import spotlight.spotlight_ver2.entity.User;
import spotlight.spotlight_ver2.enums.Role;
import spotlight.spotlight_ver2.exception.DuplicateUsernameException;
import spotlight.spotlight_ver2.repository.UserRepository;
import spotlight.spotlight_ver2.request.CertificateRequest;
import spotlight.spotlight_ver2.request.EmailSendingRequest;
import spotlight.spotlight_ver2.request.ExistIdRequest;
import spotlight.spotlight_ver2.response.CertificateResponse;
import spotlight.spotlight_ver2.response.ErrorResponse;
import spotlight.spotlight_ver2.response.ExistIdResponse;
import spotlight.spotlight_ver2.response.UserResponse;
import spotlight.spotlight_ver2.service.UserService;
import spotlight.spotlight_ver2.service.AuthenticationService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.Principal;

@RestController
@RequestMapping("/api/user")
@Tag(name = "User API", description = "회원 관련 기능 제공")
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;
    private final AuthenticationService authenticationService;
    private final UserRepository userRepository;
    private final CertificateRequest certificateRequest;

    @Autowired
    public UserController(UserService userService, AuthenticationService authenticationService, UserRepository userRepository, CertificateRequest certificateRequest) {
        this.userService = userService;
        this.authenticationService = authenticationService;
        this.userRepository = userRepository;
        this.certificateRequest = certificateRequest;
    }

    // 회원가입
    @Operation(summary = "회원가입", description = "회원 정보를 받아 새로운 사용자를 등록합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "회원가입 성공"),
            @ApiResponse(responseCode = "409", description = "아이디 중복"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody UserRegistrationDTO registrationDto) {
        final Logger logger = LoggerFactory.getLogger(UserController.class);
        try {
            User user = userService.registerNewUser(registrationDto);
            if (registrationDto.getRole() == Role.NORMAL) {
                return ResponseEntity.ok(new UserResponse(user, true, "가입이 완료되었습니다. 자동 로그인이 설정되었습니다."));
            } else {
                return ResponseEntity.ok(new UserResponse(user, true, "가입이 완료되었습니다. 이메일 인증을 진행해주세요."));
            }
        } catch (DuplicateUsernameException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new ErrorResponse(e.getMessage()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(new ErrorResponse(e.getMessage()));
        } catch (RuntimeException e) {
            logger.error("회원가입 처리 중 오류 발생: " + e.getMessage());
            return ResponseEntity.internalServerError()
                    .body(new ErrorResponse("서버에서 오류가 발생했습니다. 나중에 다시 시도해주세요."));
        }
    }

    // 아이디 중복 체크
    @Operation(summary = "아이디 중복 확인", description = "요청받은 아이디가 이미 사용 중인지 확인합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "아이디 사용 가능 여부 반환")
    })
    @PostMapping("/existusername")
    public ResponseEntity<?> existUsername(@RequestBody ExistIdRequest existIdRequest) {
        return ResponseEntity.ok(new ExistIdResponse(userService.existUsername(existIdRequest.getUsername())));
    }

    // 비밀번호 유효성 확인
    @Operation(summary = "비밀번호 유효성 검사", description = "비밀번호 유효성을 확인하고 결과를 반환합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "비밀번호 유효성 검사 결과 반환"),
            @ApiResponse(responseCode = "400", description = "잘못된 비밀번호"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @PostMapping("/validate-password")
    public ResponseEntity<PasswordValidationResponseDTO> validatePassword(@RequestBody PasswordValidationDTO passwordDTO) {
        try {
            PasswordValidationResponseDTO response = userService.validatePassword(passwordDTO.getPassword());
            if (response.isValid()) {
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.badRequest().body(response);
            }
        } catch (Exception e) {
            logger.error("비밀번호 유효성 검사 중 오류 발생: ", e);
            return ResponseEntity.internalServerError()
                    .body(PasswordValidationResponseDTO.failure("서버에서 오류가 발생했습니다."));
        }
    }

    // 이메일 인증 코드 발송
    @Operation(summary = "이메일 인증 코드 발송", description = "사용자에게 이메일 인증 코드를 발송합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "이메일 인증 코드 발송 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청, 이메일 전송 실패")
    })
    @PostMapping("/send-email-verification")
    public ResponseEntity<?> sendEmailVerification(@RequestBody EmailSendingRequest emailSendingRequest) {
        try {
            if (emailSendingRequest.getEmail() == null || emailSendingRequest.getUsername() == null) {
                return ResponseEntity.badRequest().body("이메일과 사용자명은 필수 항목입니다.");
            }

            boolean success = authenticationService.sendEmailVerificationCode(emailSendingRequest, emailSendingRequest.getRole());
            if (success) {
                return ResponseEntity.ok("이메일 인증 코드가 발송되었습니다.");
            } else {
                return ResponseEntity.badRequest().body("이메일 발송에 실패했습니다.");
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("서버 오류가 발생했습니다.");
        }
    }

    // 이메일 인증 코드 검증
    @Operation(summary = "이메일 인증 코드 검증", description = "사용자가 입력한 이메일 인증 코드를 검증합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "이메일 인증 코드 검증 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 인증 코드")
    })
    @PostMapping("/verify-email-verification")
    public ResponseEntity<?> verifyEmailVerification(@RequestBody EmailSendingRequest emailSendingRequest) {
        try {
            boolean success = authenticationService.verifyEmailCode(emailSendingRequest, emailSendingRequest.getRole());
            if (success) {
                return ResponseEntity.ok("이메일 인증이 완료되었습니다.");
            } else {
                return ResponseEntity.badRequest().body("잘못된 인증 코드입니다.");
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("서버 오류가 발생했습니다.");
        }
    }

    @Operation(summary = "학생 재학증명서 업로드", description = "학생의 재학증명서를 업로드합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "재학증명서 업로드 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @PostMapping("/upload-student-certificate")
    public ResponseEntity<CertificateResponse> uploadStudentCertificate(
            @RequestBody CertificateRequest certificateRequest) {

        String username = certificateRequest.getUsername();

        User currentUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        CertificateResponse response = userService.uploadStudentCertificate(currentUser, certificateRequest);

        return ResponseEntity.ok(response);
    }


    @Operation(summary = "리크루터 재직증명서 업로드", description = "리크루터의 재직증명서를 업로드합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "재직증명서 업로드 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @PostMapping("/upload-recruiter-certificate")
    public ResponseEntity<CertificateResponse> uploadRecruiterCertificate(
            @ModelAttribute CertificateRequest certificateRequest) {
        User currentUser = userService.getUserByUsername(certificateRequest.getUsername());
        if (currentUser == null) {
            throw new RuntimeException("사용자를 찾을 수 없습니다.");
        }
        CertificateResponse response = userService.uploadRecruiterCertificate(currentUser, certificateRequest);
        return ResponseEntity.ok(response);
    }
}