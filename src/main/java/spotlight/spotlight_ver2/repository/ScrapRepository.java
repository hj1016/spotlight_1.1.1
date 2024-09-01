package spotlight.spotlight_ver2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import spotlight.spotlight_ver2.entity.Feed;
import spotlight.spotlight_ver2.entity.Scrap;
import spotlight.spotlight_ver2.entity.Student;
import spotlight.spotlight_ver2.entity.User;

import java.util.Optional;

public interface ScrapRepository extends JpaRepository<Scrap, Long> {
    boolean existsByUserAndFeed(User user, Feed feed);
    Optional<Scrap> findByUserAndFeed(User user, Feed feed);
    boolean existsByUserAndScrappedUser(User user, Student scrappedUser);
    Optional<Scrap> findByUserAndScrappedUser(User user, Student scrappedUser);
}