package spotlight.spotlight_ver2.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import spotlight.spotlight_ver2.entity.Category;
import spotlight.spotlight_ver2.entity.Feed;
import spotlight.spotlight_ver2.entity.User;
import spotlight.spotlight_ver2.repository.CategoryRepository;
import spotlight.spotlight_ver2.repository.FeedRepository;
import spotlight.spotlight_ver2.repository.UserRepository;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RecommendationService {

    private final UserRepository userRepository;
    private final FeedRepository feedRepository;
    private final CategoryRepository categoryRepository;

    @Autowired
    public RecommendationService(UserRepository userRepository, FeedRepository feedRepository, CategoryRepository categoryRepository) {
        this.userRepository = userRepository;
        this.feedRepository = feedRepository;  // 'FeedRepository' 주입
        this.categoryRepository = categoryRepository;
    }

    // 1. 인재 추천
    // 1) 키워드가 포함된 피드를 작성한 학생을 찾기
    public List<User> recommendUsersByKeyword(String keyword) {
        System.out.println("searchFeedsByKeyword - 키워드: " + keyword);
        List<Feed> feeds = feedRepository.findByTitleContainingOrContentContaining(keyword);
        return userRepository.findByFeedsIn(feeds);  // 해당 피드를 작성한 학생들 반환
    }

    // 2) 카테고리를 기준으로 학생 추천 (카테고리에 해당하는 피드를 작성한 학생들)
    public List<User> recommendUsersByCategory(String categoryName) {
        // 카테고리 이름으로 해당 카테고리를 찾기
        Category category = categoryRepository.findByName(categoryName)
                .orElseThrow(() -> new IllegalArgumentException("카테고리를 찾을 수 없습니다."));

        // 해당 카테고리에 속하는 피드를 찾기
        List<Feed> feeds = feedRepository.findByCategory(category);

        // 해당 피드를 작성한 학생들을 반환
        return userRepository.findByFeedsIn(feeds);
    }

    // 2. 피드 추천
    // 1) 피드 제목 또는 내용에 키워드가 포함된 피드를 찾기
    public List<Feed> searchFeedsByKeyword(String keyword) {
        return feedRepository.findByTitleContainingOrContentContaining(keyword)
                .stream()
                .sorted(Comparator.comparing(Feed::getCreatedDate).reversed())
                .limit(5)
                .collect(Collectors.toList());
    }

    // 2) 해시태그를 기준으로 피드 추천
    public List<Feed> searchFeedsByHashtag(List<String> hashtag) {
        return feedRepository.findByHashtagsHashtag(hashtag)
                .stream()
                .sorted(Comparator.comparing(Feed::getCreatedDate).reversed())
                .limit(5)
                .collect(Collectors.toList());
    }

}