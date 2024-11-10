package spotlight.spotlight_ver2.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import spotlight.spotlight_ver2.request.AuthenticationRequest;
import spotlight.spotlight_ver2.security.JwtUtil;
import spotlight.spotlight_ver2.service.AuthenticationService;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
@Tag(name = "Authentication API", description = "사용자 인증 관리 API")
public class AuthenticationController {
    private final AuthenticationService authenticationService;
    private final JwtUtil jwtUtil;

    @Autowired
    public AuthenticationController(AuthenticationService authenticationService, JwtUtil jwtUtil) {
        this.authenticationService = authenticationService;
        this.jwtUtil = jwtUtil;
    }

    @Operation(summary = "로그인 요청", description = "사용자 이름과 비밀번호를 통해 사용자를 인증하고 JWT 토큰을 반환합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "로그인 성공, JWT 토큰 반환"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청, 로그인 실패")
    })
    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody AuthenticationRequest request) {
        try {
            String accessToken = authenticationService.authenticateUser(request.getUsername(), request.getPassword());
            String refreshToken = jwtUtil.generateRefreshToken(request.getUsername());

            Map<String, String> response = new HashMap<>();
            response.put("accessToken", "Bearer " + accessToken);
            response.put("refreshToken", "Bearer " + refreshToken);

            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(summary = "리프레시 토큰으로 액세스 토큰 갱신", description = "리프레시 토큰을 통해 새로운 액세스 토큰을 발급받습니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "액세스 토큰 갱신 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청, 리프레시 토큰 만료 또는 유효하지 않음")
    })
    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshAccessToken(@RequestBody String refreshToken) {
        try {
            String newAccessToken = jwtUtil.refreshAccessToken(refreshToken);

            Map<String, String> response = new HashMap<>();
            response.put("accessToken", "Bearer " + newAccessToken);

            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}