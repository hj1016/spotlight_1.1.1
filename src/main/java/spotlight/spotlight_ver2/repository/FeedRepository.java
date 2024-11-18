package spotlight.spotlight_ver2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import spotlight.spotlight_ver2.entity.Category;
import spotlight.spotlight_ver2.entity.Feed;
import spotlight.spotlight_ver2.entity.User;

import java.util.List;
import java.util.Optional;

public interface FeedRepository extends JpaRepository<Feed, Long> {
    @Query("SELECT f FROM Feed f " +
            "JOIN FETCH f.user u " +
            "JOIN FETCH f.category c " +
            "LEFT JOIN FETCH f.exhibition e " +
            "LEFT JOIN FETCH f.project p " +
            "LEFT JOIN FETCH f.hashtags h " +
            "WHERE f.id = :id")
    Optional<Feed> findByIdWithAllRelations(@Param("id") Long id);

    @Query("SELECT DISTINCT f FROM Feed f " +
            "JOIN FETCH f.user u " +
            "JOIN FETCH f.category c " +
            "LEFT JOIN FETCH f.exhibition e " +
            "LEFT JOIN FETCH f.project p " +
            "LEFT JOIN FETCH f.hashtags h " +
            "WHERE h.id = :hashtagId")
    List<Feed> findFeedsByHashtagIdWithRelations(@Param("hashtagId") Long hashtagId);

    // 챗봇 검색
    @Query("SELECT f FROM Feed f WHERE f.title LIKE %:keyword% OR f.content LIKE %:keyword%")
    List<Feed> findByTitleContainingOrContentContaining(@Param("keyword") String keyword);
    @Query("SELECT f FROM Feed f JOIN f.hashtags h WHERE h.hashtag IN :hashtags")
    List<Feed> findByHashtagsHashtag(@Param("hashtags") List<String> hashtags);
    List<Feed> findByCategory(Category category);
    Optional<Feed> findTopByUserOrderByCreatedDateDesc(User user);

    // 학교와 학과로 검색
    @Query("SELECT f FROM Feed f " +
            "JOIN f.user u " +
            "JOIN Student s ON u.id = s.userId " +
            "WHERE s.school = :school AND s.major = :major")
    List<Feed> findFeedsBySchoolAndMajor(@Param("school") String school,
                                         @Param("major") String major);

    // 학교로 검색
    @Query("SELECT f FROM Feed f " +
            "JOIN f.user u " +
            "JOIN Student s ON u.id = s.userId " +
            "WHERE s.school = :school")
    List<Feed> findFeedsBySchool(@Param("school") String school);

    // 학과로 검색
    @Query("SELECT f FROM Feed f " +
            "JOIN f.user u " +
            "JOIN Student s ON u.id = s.userId " +
            "WHERE s.major = :major")
    List<Feed> findFeedsByMajor(@Param("major") String major);

    @Query("SELECT f FROM Feed f WHERE f.user.id IN :userIds")
    List<Feed> findByStudentIdIn(@Param("userIds") List<Long> userIds);
}