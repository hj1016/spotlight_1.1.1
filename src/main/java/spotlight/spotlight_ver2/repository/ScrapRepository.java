package spotlight.spotlight_ver2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import spotlight.spotlight_ver2.entity.*;

import java.util.List;
import java.util.Optional;

public interface ScrapRepository extends JpaRepository<Scrap, Long> {
    boolean existsByUserAndFeed(User user, Feed feed);

    Optional<Scrap> findByUserAndFeed(User user, Feed feed);

    boolean existsByUserAndScrappedUser(User user, Student scrappedUser);

    // 사용자와 단계(Stage)를 기준으로 스크랩을 찾는 메서드
    @Query("SELECT s FROM Scrap s WHERE s.user = :user AND s.stageId = :stage")
    Optional<Scrap> findByUserAndStageId(@Param("user") User user, @Param("stage") Stage stage);

    // 사용자와 스크랩된 사용자를 기준으로 스크랩을 찾는 메서드
    @Query("SELECT s FROM Scrap s WHERE s.user = :user AND s.scrappedUser = :scrappedUser")
    Optional<Scrap> findByUserAndScrappedUser(@Param("user") User user, @Param("scrappedUser") User scrappedUser);

    boolean existsByUserAndScrappedUser(User user, User scrappedUser);

    @Query("SELECT st FROM Student st JOIN Scrap s ON s.scrappedUser.id = st.user.id WHERE s.user.id = :userId")
    List<Student> findScrappedStudentsByUserId(@Param("userId") Long userId);
}