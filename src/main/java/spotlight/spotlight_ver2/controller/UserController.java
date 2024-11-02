package spotlight.spotlight_ver2.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import spotlight.spotlight_ver2.dto.PasswordValidationDTO;
import spotlight.spotlight_ver2.dto.PasswordValidationResponseDTO;
import spotlight.spotlight_ver2.dto.UserRegistrationDTO;
import spotlight.spotlight_ver2.entity.User;
import spotlight.spotlight_ver2.enums.Role;
import spotlight.spotlight_ver2.exception.DuplicateUsernameException;
import spotlight.spotlight_ver2.request.ExistIdRequest;
import spotlight.spotlight_ver2.response.ErrorResponse;
import spotlight.spotlight_ver2.response.ExistIdResponse;
import spotlight.spotlight_ver2.response.UserResponse;
import spotlight.spotlight_ver2.service.UserService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/user")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    // 회원가입
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
    @PostMapping("/existusername")
    public ResponseEntity<?> existUsername(@RequestBody ExistIdRequest existIdRequest) {
        return ResponseEntity.ok(new ExistIdResponse(userService.existUsername(existIdRequest.getUsername())));
    }

    // 비밀번호 유효성 확인
    @PostMapping("/validate-password")
    public ResponseEntity<PasswordValidationResponseDTO> validatePassword(@RequestBody PasswordValidationDTO passwordDTO) {
        PasswordValidationResponseDTO response = userService.validatePassword(passwordDTO.getPassword());
        return ResponseEntity.ok(response);
    }
}
