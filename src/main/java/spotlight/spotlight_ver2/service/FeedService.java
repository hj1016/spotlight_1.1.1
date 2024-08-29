package spotlight.spotlight_ver2.service;

import spotlight.spotlight_ver2.dto.*;
import spotlight.spotlight_ver2.entity.Feed;
import spotlight.spotlight_ver2.entity.Hashtag;
import spotlight.spotlight_ver2.entity.User;
import spotlight.spotlight_ver2.exception.*;
import spotlight.spotlight_ver2.mapper.FeedMapper;
import spotlight.spotlight_ver2.repository.FeedRepository;
import spotlight.spotlight_ver2.repository.HashtagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import spotlight.spotlight_ver2.repository.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FeedService {

    private final FeedRepository feedRepository;
    private final HashtagRepository hashtagRepository;
    private final SearchHistoryService searchHistoryService;
    private final UserRepository userRepository;
    private final FeedMapper feedMapper = FeedMapper.INSTANCE;

    @Autowired
    public FeedService(FeedRepository feedRepository, HashtagRepository hashtagRepository, SearchHistoryService searchHistoryService, UserRepository userRepository) {
        this.feedRepository = feedRepository;
        this.hashtagRepository = hashtagRepository;
        this.searchHistoryService = searchHistoryService;
        this.userRepository = userRepository;
    }

    public FeedDTO createFeed(FeedDTO feedDTO) {
        User user = userRepository.findById(feedDTO.getUser().getId())
                .orElseThrow(() -> new NotFoundException("사용자를 찾을 수 없습니다."));

        Feed feed = feedMapper.toEntity(feedDTO);
        validateFeed(feed);
        feed.setUser(user);
        Feed createdFeed = feedRepository.save(feed);
        return feedMapper.toDTO(createdFeed);
    }

    public FeedDTO getFeedById(Long id) {
        Feed feed = feedRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("해당 게시글을 찾을 수 없습니다."));
        return feedMapper.toDTO(feed);
    }

    public FeedDTO updateFeed(Long id, FeedDTO feedDTO) {
        Feed existingFeed = feedRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("해당 게시글을 찾을 수 없습니다."));

        User user = userRepository.findById(feedDTO.getUser().getId())
                .orElseThrow(() -> new NotFoundException("사용자를 찾을 수 없습니다."));

        if (!existingFeed.getUser().equals(user)) {
            throw new UnauthorizedException("게시글을 수정할 권한이 없습니다.");
        }

        Feed updatedFeed = feedMapper.toEntity(feedDTO);
        updatedFeed.setFeedId(existingFeed.getFeedId()); // Retain the existing ID
        updatedFeed.setUser(existingFeed.getUser()); // Retain the existing user

        validateFeed(updatedFeed);
        Feed savedFeed = feedRepository.save(updatedFeed);
        return feedMapper.toDTO(savedFeed);
    }

    public void deleteFeed(Long id, User userDTO) {
        Feed feed = feedRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("해당 게시글을 찾을 수 없습니다."));

        User user = userRepository.findById(userDTO.getId())
                .orElseThrow(() -> new NotFoundException("사용자를 찾을 수 없습니다."));

        // 권한 검증
        if (!feed.getUser().equals(user)) {
            throw new UnauthorizedException("게시글을 삭제할 권한이 없습니다.");
        }

        feedRepository.delete(feed);
    }

    private void validateFeed(Feed feed) {
        if (feed.getTitle() == null || feed.getTitle().isEmpty() ||
                feed.getThiumbnailImage() == null || feed.getThiumbnailImage().isEmpty() ||
                feed.getContent() == null || feed.getContent().isEmpty()) {
            throw new BadRequestException("게시글 제목, 이미지 URL, 내용은 필수 입력 사항입니다.");
        }

        if (!isValidUrl(feed.getThiumbnailImage())) {
            throw new BadRequestException("유효하지 않은 이미지 URL입니다. 올바른 URL을 입력하세요.");
        }

        if (feed.getCategory() == null) {
            throw new BadRequestException("카테고리가 지정되지 않았습니다. 카테고리를 선택하세요.");
        }

        // Validate the presence of project or exhibition
        if (feed.getProject() == null && feed.getExhibition() == null) {
            throw new BadRequestException("프로젝트 또는 전시 정보 중 하나는 필수입니다.");
        }
    }

    private boolean isValidUrl(String url) {
        return url.startsWith("http://") || url.startsWith("https://");
    }

    /**
     * 해시태그로 피드를 검색하는 메서드
     *
     * @param hashtag 검색할 해시태그
     * @return 해시태그가 포함된 피드 목록
     * @throws NotFoundException 해시태그를 찾을 수 없는 경우 발생
     * @throws SearchHistoryException 검색 중 오류 발생 시 발생
     */
    public List<FeedDTO> searchFeedsByHashtag(String hashtag) {
        try {
            // 해시태그를 검색
            Optional<Hashtag> hashtagOpt = hashtagRepository.findByHashtag(hashtag);
            if (hashtagOpt.isEmpty()) {
                throw new NotFoundException("해시태그를 찾을 수 없습니다: " + hashtag);
            }
            Hashtag hashtagEntity = hashtagOpt.get();

            // 검색 기록에 해시태그를 추가
            searchHistoryService.addSearchHistory(hashtag);

            // 해시태그 ID를 통해 피드를 검색
            List<Feed> feeds = feedRepository.findByHashtagsId(hashtagEntity.getId());

            // 검색된 피드를 DTO로 변환하여 반환
            return feeds.stream()
                    .map(feedMapper::toDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new SearchHistoryException("피드를 검색하는 동안 오류가 발생했습니다: " + e.getMessage());
        }
    }
}