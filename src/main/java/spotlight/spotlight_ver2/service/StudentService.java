package spotlight.spotlight_ver2.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import spotlight.spotlight_ver2.entity.Category;
import spotlight.spotlight_ver2.entity.Feed;
import spotlight.spotlight_ver2.entity.Student;
import spotlight.spotlight_ver2.repository.FeedRepository;

import java.util.Optional;

@Service
public class StudentService {

    private final FeedRepository feedRepository;

    @Autowired
    public StudentService(FeedRepository feedRepository) {
        this.feedRepository = feedRepository;
    }

    // 학생의 분야를 결정하는 메서드
    public Optional<Category> findMostRecentFeedCategory(Student student) {
        // Student의 최근 Feed 조회
        Optional<Feed> recentFeed = feedRepository.findTopByUserOrderByCreatedDateDesc(student.getUser());
        return recentFeed.map(Feed::getCategory); // Feed가 존재하면 Category 반환
    }
}