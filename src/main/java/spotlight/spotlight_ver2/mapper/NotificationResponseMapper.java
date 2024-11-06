package spotlight.spotlight_ver2.mapper;

import org.springframework.stereotype.Component;
import spotlight.spotlight_ver2.entity.Notification;
import spotlight.spotlight_ver2.response.NotificationResponse;

import java.time.Duration;
import java.time.LocalDateTime;

@Component
public class NotificationResponseMapper {

    public NotificationResponse toResponse(Notification notification) {
        String relativeTime = getRelativeTime(notification.getCreatedDate());
        return new NotificationResponse(
                notification.getNotificationId(),
                notification.getType(),
                notification.getMessage(),
                relativeTime,
                notification.getStatus()
        );
    }

    private String getRelativeTime(LocalDateTime dateTime) {
        LocalDateTime now = LocalDateTime.now();
        Duration duration = Duration.between(dateTime, now);
        long days = duration.toDays();
        if (days == 0) {
            return "Today";
        } else if (days == 1) {
            return "Yesterday";
        } else {
            return days + " days ago";
        }
    }
}
