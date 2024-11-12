package spotlight.spotlight_ver2.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import spotlight.spotlight_ver2.entity.Notification;
import spotlight.spotlight_ver2.entity.User;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByReceiver(User receiver);

    List<Notification> findByReceiverAndStatus(User receiver, String status);

    List<Notification> findByReceiver(User receiver, Pageable pageable);
}