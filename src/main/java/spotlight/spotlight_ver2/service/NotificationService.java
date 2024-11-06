package spotlight.spotlight_ver2.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spotlight.spotlight_ver2.config.NotificationConstants;
import spotlight.spotlight_ver2.entity.Notification;
import spotlight.spotlight_ver2.entity.User;
import spotlight.spotlight_ver2.exception.NotFoundException;
import spotlight.spotlight_ver2.repository.NotificationRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class NotificationService {
    private final NotificationRepository notificationRepository;

    @Autowired
    public NotificationService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    @Transactional
    public Notification createNotification(User sender, User receiver, String type, String message) {
        Notification notification = new Notification();
        notification.setSender(sender);
        notification.setReceiver(receiver);
        notification.setType(type);
        notification.setStatus(NotificationConstants.STATUS_UNREAD);
        notification.setCreatedDate(LocalDateTime.now());
        notification.setMessage(message);
        return notificationRepository.save(notification);
    }

    public List<Notification> getNotificationsForUser(User receiver) {
        return notificationRepository.findByReceiver(receiver);
    }

    public boolean markAsRead(long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new NotFoundException("알림을 찾을 수 없습니다."));
        notification.setStatus(NotificationConstants.STATUS_READ);
        notificationRepository.save(notification);
        return true;
    }
}
