package spotlight.spotlight_ver2.service;

import spotlight.spotlight_ver2.dto.CategoryDTO;
import spotlight.spotlight_ver2.dto.FeedDTO;
import spotlight.spotlight_ver2.dto.HashtagDTO;
import spotlight.spotlight_ver2.dto.UserDTO;
import spotlight.spotlight_ver2.entity.Feed;
import spotlight.spotlight_ver2.entity.Hashtag;
import spotlight.spotlight_ver2.exception.NotFoundException;
import spotlight.spotlight_ver2.exception.SearchHistoryException;
import spotlight.spotlight_ver2.repository.FeedRepository;
import spotlight.spotlight_ver2.repository.HashtagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FeedService {

    @Autowired
    private FeedRepository feedRepository;

    @Autowired
    private HashtagRepository hashtagRepository;

    @Autowired
    private SearchHistoryService searchHistoryService;

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
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new SearchHistoryException("피드를 검색하는 동안 오류가 발생했습니다: " + e.getMessage());
        }
    }

    /**
     * Feed 엔티티를 FeedDTO로 변환하는 메서드
     *
     * @param feed 변환할 Feed 엔티티
     * @return 변환된 FeedDTO
     */
    private FeedDTO convertToDTO(Feed feed) {
        // Feed 엔티티의 정보를 DTO로 변환하여 반환
        return new FeedDTO(
                feed.getFeedId(),
                feed.getTitle(),
                feed.getThiumbnailImage(),
                feed.getFeedImages(),
                feed.getContent(),
                feed.getScrap(),
                feed.getHitsUser(),
                feed.getHitsRecruiter(),
                new CategoryDTO(
                        feed.getCategory().getId(),
                        feed.getCategory().getName(),
                        null, // 부모 카테고리 정보가 필요하다면 설정
                        null // FeedDTO의 카테고리와 관련된 Feed 정보 설정
                ),
                feed.getCreatedDate(),
                feed.getModifiedDate(),
                new UserDTO(
                        feed.getUser().getId(),
                        feed.getUser().getUsername(),
                        feed.getUser().getEmail(),
                        feed.getUser().getName(),
                        feed.getUser().getProfileImage(),
                        feed.getUser().getCreatedDate(),
                        feed.getUser().getUpdatedDate(),
                        null, // Hashtags 정보 설정
                        null, // Feeds 정보 설정
                        null // Scraps 정보 설정
                ),
                null, // ExhibitionDTO 설정
                null, // ProjectDTO 설정
                feed.getHashtags().stream()
                        .map(hashtag -> new HashtagDTO(
                                hashtag.getId(),
                                hashtag.getHashtag(),
                                hashtag.getCreatedDate(),
                                new UserDTO(
                                        hashtag.getUser().getId(),
                                        hashtag.getUser().getUsername(),
                                        hashtag.getUser().getEmail(),
                                        hashtag.getUser().getName(),
                                        hashtag.getUser().getProfileImage(),
                                        hashtag.getUser().getCreatedDate(),
                                        hashtag.getUser().getUpdatedDate(),
                                        null, // Hashtags 정보 설정
                                        null, // Feeds 정보 설정
                                        null  // Scraps 정보 설정
                                )
                        ))
                        .collect(Collectors.toSet())
        );
    }
}