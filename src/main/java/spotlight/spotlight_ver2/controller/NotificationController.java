package spotlight.spotlight_ver2.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import spotlight.spotlight_ver2.entity.User;
import spotlight.spotlight_ver2.response.ErrorResponse;
import spotlight.spotlight_ver2.response.NotificationResponse;
import spotlight.spotlight_ver2.service.NotificationService;
import spotlight.spotlight_ver2.mapper.NotificationResponseMapper;
import spotlight.spotlight_ver2.service.UserService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/notification")
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
    public ResponseEntity<?> getNotifications() {
        try {
            User receiver = userService.getCurrentUser();
            List<NotificationResponse> response = notificationService.getNotificationsForUser(receiver)
                    .stream()
                    .map(notificationResponseMapper::toResponse)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(new ErrorResponse("서버에서 오류가 발생했습니다. 나중에 다시 시도해주세요."));
        }
    }

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
