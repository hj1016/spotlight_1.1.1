package spotlight.spotlight_ver2.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import spotlight.spotlight_ver2.entity.User;
import spotlight.spotlight_ver2.response.ErrorResponse;
import spotlight.spotlight_ver2.response.NotificationResponse;
import spotlight.spotlight_ver2.service.NotificationService;
import spotlight.spotlight_ver2.mapper.NotificationResponseMapper;
import spotlight.spotlight_ver2.service.UserService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/notification")
@Tag(name = "알림 API", description = "알림 관련 기능 제공")
public class NotificationController {

    private final NotificationService notificationService;
    private final UserService userService;
    private final NotificationResponseMapper notificationResponseMapper;

    @Autowired
    public NotificationController(NotificationService notificationService,
                                  UserService userService,
                                  NotificationResponseMapper notificationResponseMapper) {
        this.notificationService = notificationService;
        this.userService = userService;
        this.notificationResponseMapper = notificationResponseMapper;
    }

    @GetMapping
    @Operation(summary = "알림 목록 조회", description = "현재 사용자에게 수신된 알림 목록을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "알림 목록 조회 성공"),
            @ApiResponse(responseCode = "500", description = "서버 오류로 조회 실패")
    })
    public ResponseEntity<?> getNotifications() {
        try {
            User receiver = userService.getCurrentUser();
            List<NotificationResponse> notifications = notificationService.getNotificationsForUser(receiver)
                    .stream()
                    .map(notificationResponseMapper::toResponse)
                    .collect(Collectors.toList());
            Map<String, Object> response = new HashMap<>();
            response.put("notifications", notifications);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(new ErrorResponse("서버에서 오류가 발생했습니다. 나중에 다시 시도해주세요."));
        }
    }

    @Operation(summary = "알림 읽음 상태로 표시", description = "특정 알림을 \"읽음\" 상태로 변경합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "알림을 읽음 상태로 표시 성공"),
            @ApiResponse(responseCode = "404", description = "알림을 찾을 수 없음")
    })
    @PutMapping("/{notificationId}/status")
    public ResponseEntity<String> markAsRead(@PathVariable("notificationId") long notificationId) {
        try {
            notificationService.markAsRead(notificationId);
            return ResponseEntity.ok("알림을 \"읽음\"으로 표시했습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(404).body("알림을 찾을 수 없습니다.");
        }
    }
}
