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

    // 제목이나 내용에 특정 키워드가 포함된 Feed 검색
    List<Feed> findByTitleContainingOrContentContaining(String title, String content);

    // 특정 카테고리에 속하는 Feed 검색
    List<Feed> findByCategory(Category category);

    // 특정 해시태그가 포함된 Feed 검색
    List<Feed> findByHashtagsHashtag(String hashtag);

    // 학생이 참여한 가장 최근의 Feed를 가져오는 메서드
    Optional<Feed> findTopByUserOrderByCreatedDateDesc(User user);
}