package spotlight.spotlight_ver2.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import spotlight.spotlight_ver2.entity.Category;
import spotlight.spotlight_ver2.entity.Feed;
import spotlight.spotlight_ver2.entity.User;
import spotlight.spotlight_ver2.repository.FeedRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RecommendationService {

    private final FeedRepository feedRepository;

    @Autowired
    public RecommendationService(FeedRepository feedRepository) {
        this.feedRepository = feedRepository;
    }

    // 카테고리를 기반으로 학생 추천
    public List<User> recommendUsersByCategory(Category category) {
        List<Feed> feeds = feedRepository.findByCategory(category);
        return feeds.stream()
                .map(Feed::getUser)  // Feed에서 User를 가져옴
                .distinct()           // 중복 제거
                .limit(3)             // 상위 3명만 추천
                .collect(Collectors.toList());
    }

    // 키워드를 기반으로 학생 추천
    public List<User> recommendUsersByKeyword(String keyword) {
        List<Feed> feeds = feedRepository.findByTitleContainingOrContentContaining(keyword, keyword);
        return feeds.stream()
                .map(Feed::getUser)  // Feed에서 User를 가져옴
                .distinct()           // 중복 제거
                .limit(3)             // 상위 3명만 추천
                .collect(Collectors.toList());
    }
}