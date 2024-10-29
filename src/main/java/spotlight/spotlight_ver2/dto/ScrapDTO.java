package spotlight.spotlight_ver2.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import spotlight.spotlight_ver2.entity.Feed;
import spotlight.spotlight_ver2.entity.Scrap;
import spotlight.spotlight_ver2.entity.Stage;
import spotlight.spotlight_ver2.entity.User;

@Getter
@Setter
@Schema(description = "스크랩 DTO")
public class ScrapDTO {

    @Schema(description = "스크랩 ID", example = "1")
    private Long scrapId;

    @Schema(description = "스크랩한 사용자 정보", implementation = ScrapUserDTO.class)
    private ScrapUserDTO user;

    @Schema(description = "스크랩된 피드 정보", implementation = ScrapFeedDTO.class)
    private ScrapFeedDTO feed;

    @Schema(description = "스크랩된 스테이지 정보", implementation = ScrapStageDTO.class)
    private ScrapStageDTO stage;

    @Schema(description = "스크랩된 인재 정보", implementation = ScrappedUserDTO.class)
    private ScrappedUserDTO scrappedUser;

    public ScrapDTO(Scrap scrap) {
        this.scrapId = scrap.getScrapId();
        this.user = (scrap.getUser() != null) ? new ScrapUserDTO(scrap.getUser()) : null;
        this.feed = (scrap.getFeed() != null) ? new ScrapFeedDTO(scrap.getFeed()) : null;
        this.stage = (scrap.getStageId() != null) ? new ScrapStageDTO(scrap.getStageId()) : null;
        this.scrappedUser = (scrap.getScrappedUser() != null) ? new ScrappedUserDTO(scrap.getScrappedUser()) : null;
    }

    @Getter
    public static class ScrapUserDTO extends UserBaseDTO {
        public ScrapUserDTO(User user) {
            super(user);
        }
    }

    @Getter
    public static class ScrappedUserDTO extends UserBaseDTO {
        public ScrappedUserDTO(User user) {
            super(user);
        }
    }

    @Getter
    public static class UserBaseDTO {
        @Schema(description = "사용자 ID", example = "1")
        private Long id;

        @Schema(description = "사용자 이름", example = "김학생")
        private String username;

        @Schema(description = "이메일 주소", example = "김학생@example.com")
        private String email;

        @Schema(description = "사용자 실명", example = "김학생")
        private String name;

        public UserBaseDTO(User user) {
            this.id = user.getId();
            this.username = user.getUsername();
            this.email = user.getEmail();
            this.name = user.getName();
        }
    }

    @Getter
    public static class ScrapFeedDTO {
        @Schema(description = "피드 ID", example = "1")
        private Long id;

        @Schema(description = "피드 제목", example = "프로젝트 제안")
        private String title;

        @Schema(description = "썸네일 이미지 URL", example = "http://example.com/thumbnail.jpg")
        private String thumbnailImage;

        @Schema(description = "피드 내용", example = "이것은 프로젝트 제안입니다.")
        private String content;

        public ScrapFeedDTO(Feed feed) {
            this.id = feed.getFeedId();
            this.title = feed.getTitle();
            this.thumbnailImage = feed.getThumbnailImage();
            this.content = feed.getContent();
        }
    }

    @Getter
    public static class ScrapStageDTO {
        @Schema(description = "스테이지 ID", example = "1")
        private Long id;

        public ScrapStageDTO(Stage stage) {
            this.id = stage.getId();
        }
    }
}