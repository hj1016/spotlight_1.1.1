package spotlight.spotlight_ver2.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import spotlight.spotlight_ver2.entity.Feed;
import spotlight.spotlight_ver2.entity.Hashtag;
import spotlight.spotlight_ver2.entity.User;

import java.sql.Timestamp;
import java.util.Set;

@Getter
@Setter
@Schema(description = "해시태그 DTO")
public class HashtagDTO {

    @Schema(description = "해시태그 ID", example = "1")
    private Long id;

    @Schema(description = "해시태그 이름", example = "#design")
    private String hashtag;

    @Schema(description = "생성 일자", example = "2024-09-06T12:00:00Z")
    private Timestamp createdDate;

    @Schema(description = "사용자 정보", implementation = HashtagUserDTO.class)
    private HashtagUserDTO user;

    @Schema(description = "해시태그가 포함된 피드들")
    private Set<HashtagFeedDTO> feeds;

    public HashtagDTO() {}

    public HashtagDTO(Hashtag hashtag) {
        this.id = hashtag.getId();
        this.hashtag = hashtag.getHashtag();
        this.createdDate = hashtag.getCreatedDate();

        // 사용자가 null이 아닐 경우에만 초기화
        if (hashtag.getUser() != null) {
            this.user = new HashtagUserDTO(hashtag.getUser());
        }
    }

    @Getter
    @Setter
    public static class HashtagUserDTO {
        @Schema(description = "사용자 ID", example = "1")
        private Long id;

        @Schema(description = "사용자 이름", example = "김학생")
        private String username;

        public HashtagUserDTO() {}

        public HashtagUserDTO(User user) {
            this.id = user.getId();
            this.username = user.getUsername();
        }
    }

    @Getter
    public static class HashtagFeedDTO {
        @Schema(description = "피드 ID", example = "1")
        private Long feedId;

        @Schema(description = "피드 제목", example = "Feed Title")
        private String title;

        public HashtagFeedDTO() {}

        public HashtagFeedDTO(Feed feed) {
            this.feedId = feed.getFeedId();
            this.title = feed.getTitle();
        }
    }
}