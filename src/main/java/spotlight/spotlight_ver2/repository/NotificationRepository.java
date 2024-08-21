package spotlight.spotlight_ver2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import spotlight.spotlight_ver2.entity.Notification;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
}