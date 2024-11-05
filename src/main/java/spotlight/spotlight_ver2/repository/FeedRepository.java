package spotlight.spotlight_ver2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import spotlight.spotlight_ver2.entity.Category;
import spotlight.spotlight_ver2.entity.Feed;
import spotlight.spotlight_ver2.entity.User;

import java.util.List;
import java.util.Optional;

public interface FeedRepository extends JpaRepository<Feed, Long> {
    List<Feed> findByHashtagsId(Long hashtagId);

    // 제목이나 내용에 특정 키워드가 포함된 Feed 검색
    List<Feed> findByTitleContainingOrContentContaining(String title, String content);

    // 특정 카테고리에 속하는 Feed 검색
    List<Feed> findByCategory(Category category);

    // 특정 해시태그가 포함된 Feed 검색
    List<Feed> findByHashtagsHashtag(String hashtag);

    // 학생이 참여한 가장 최근의 Feed를 가져오는 메서드
    Optional<Feed> findTopByUserOrderByCreatedDateDesc(User user);
}