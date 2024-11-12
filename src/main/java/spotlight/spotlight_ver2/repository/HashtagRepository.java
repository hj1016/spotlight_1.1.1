package spotlight.spotlight_ver2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import spotlight.spotlight_ver2.entity.Hashtag;

import java.util.List;

@Repository
public interface HashtagRepository extends JpaRepository<Hashtag, Integer> {
    List<Hashtag> findByHashtag(String hashtag);

    @Query("SELECT h FROM Hashtag h JOIN FETCH h.feeds f WHERE h.hashtag = :hashtag")
    List<Hashtag> findByHashtagWithFeeds(@Param("hashtag") String hashtag);
}