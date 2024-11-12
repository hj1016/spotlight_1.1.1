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
        if (dateTime == null) {
            return "Unknown";
        }

        LocalDateTime now = LocalDateTime.now();
        Duration duration = Duration.between(dateTime, now);

        long days = duration.toDays();
        if (days > 1) {
            return days + " days ago";
        } else if (days == 1) {
            return "Yesterday";
        } else {
            long hours = duration.toHours();
            if (hours >= 1) {
                return hours + " hours ago";
            } else {
                long minutes = duration.toMinutes();
                return minutes + " minutes ago";
            }
        }
    }
}
