package spotlight.spotlight_ver2.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import spotlight.spotlight_ver2.entity.*;

import java.sql.Timestamp;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@Schema(description = "피드 DTO")
public class FeedDTO {

    @Schema(description = "피드 ID", example = "1")
    private Long feedId;

    @Schema(description = "피드 제목", example = "Feed Title")
    private String title;

    @Schema(description = "썸네일 이미지 URL", example = "http://example.com/thumbnail.jpg")
    private String thumbnailImage;

    @Schema(description = "피드 이미지 URL 목록", example = "[\"http://example.com/image1.jpg\", \"http://example.com/image2.jpg\"]")
    private List<String> feedImages;  // String -> List<String>

    @Schema(description = "피드 내용", example = "This is the content of the feed.")
    private String content;

    @Schema(description = "스크랩 수", example = "123")
    private Integer scrap;

    @Schema(description = "사용자 조회 수", example = "456")
    private Integer hitsUser;

    @Schema(description = "리크루터 조회 수", example = "789")
    private Integer hitsRecruiter;

    @Schema(description = "생성 일자", example = "2024-09-06T12:00:00Z")
    private Timestamp createdDate;

    @Schema(description = "수정 일자", example = "2024-09-06T12:00:00Z")
    private Timestamp modifiedDate;

    @Schema(description = "카테고리", implementation = FeedCategoryDTO.class)
    private FeedCategoryDTO category;

    @Schema(description = "사용자 정보", implementation = FeedUserDTO.class)
    private FeedUserDTO user;

    @Schema(description = "전시 정보", implementation = FeedExhibitionDTO.class)
    private FeedExhibitionDTO exhibition;

    @Schema(description = "프로젝트 정보", implementation = FeedProjectDTO.class)
    private FeedProjectDTO project;

    @Schema(description = "해시태그 집합", implementation = FeedHashtagDTO.class)
    private Set<FeedHashtagDTO> hashtags;

    public FeedDTO() {}

    /* Feed -> FeedDTO */
    public FeedDTO(Feed feed) {
        this.feedId = feed.getFeedId();
        this.title = feed.getTitle();
        this.thumbnailImage = feed.getThumbnailImage();
        this.feedImages = feed.getFeedImages();
        this.content = feed.getContent();
        this.scrap = feed.getScrap();
        this.hitsUser = feed.getHitsUser();
        this.hitsRecruiter = feed.getHitsRecruiter();
        this.createdDate = feed.getCreatedDate();
        this.modifiedDate = feed.getModifiedDate();

        this.category = Optional.ofNullable(feed.getCategory())
                .map(FeedCategoryDTO::new)
                .orElse(null);
        this.user = Optional.ofNullable(feed.getUser())
                .map(FeedUserDTO::new)
                .orElse(null);
        this.exhibition = Optional.ofNullable(feed.getExhibition())
                .map(FeedExhibitionDTO::new)
                .orElse(null);
        this.project = Optional.ofNullable(feed.getProject())
                .map(FeedProjectDTO::new)
                .orElse(null);

        this.hashtags = feed.getHashtags() != null
                ? feed.getHashtags().stream().map(FeedHashtagDTO::new).collect(Collectors.toSet())
                : Collections.emptySet();
    }

    /* 참조할 CategoryDTO */
    @Getter
    @Setter
    public static class FeedCategoryDTO {
        @Schema(description = "카테고리 ID", example = "9")
        private Long id;

        @Schema(description = "카테고리 이름", example = "전기/전자")
        private String name;

        public FeedCategoryDTO() {}

        public FeedCategoryDTO(Category category){
            this.id = category.getId();
            this.name = category.getName();
        }
    }

    /* 참조할 UserDTO */
    @Getter
    @Setter
    public static class FeedUserDTO {
        @Schema(description = "사용자 ID", example = "1")
        private Long id;

        @Schema(description = "사용자 실명", example = "김학생")
        private String name;

        public FeedUserDTO() {}

        public FeedUserDTO(User user){
            this.id = user.getId();
            this.name = user.getName();
        }
    }

    /* 참조할 ExhibitionDTO */
    @Getter
    @Setter
    public static class FeedExhibitionDTO {
        @Schema(description = "전시 ID", example = "1")
        private Long exhibitionId;

        @Schema(description = "전시 위치", example = "서울여자대학교 조형예술관")
        private String location;

        @Schema(description = "전시 일정", example = "2024-08-15")
        private String schedule;

        @Schema(description = "전시 시간", example = "10:00 AM")
        private String time;

        @Schema(description = "사용자 ID", example = "123")
        private Long userId;

        @Schema(description = "피드 ID", example = "456")
        private Long feedId;

        public FeedExhibitionDTO() {}

        public FeedExhibitionDTO(Exhibition exhibition){
            this.exhibitionId = exhibition.getExhibitionId();
            this.location = exhibition.getLocation();
            this.schedule = exhibition.getSchedule();
            this.time = exhibition.getTime();
            this.userId = exhibition.getUser().getId();
            this.feedId = exhibition.getFeed().getFeedId();
        }
    }

    /* 참조할 ProjectDTO */
    @Getter
    @Setter
    public static class FeedProjectDTO {
        @Schema(description = "프로젝트 ID")
        private Long id;

        @Schema(description = "프로젝트 이름")
        private String name;

        @Schema(description = "팀원 역할 리스트", implementation = ProjectRoleDTO.class)
        private List<ProjectRoleDTO> projectRoles;

        public FeedProjectDTO() {}

        public FeedProjectDTO(Project project) {
            this.id = project.getId();
            this.name = project.getName();
            this.projectRoles = Optional.ofNullable(project.getProjectRoles())
                    .orElse(Collections.emptyMap())
                    .values().stream()
                    .map(ProjectRoleDTO::new)
                    .collect(Collectors.toList());
        }
    }

    /* 참조할 HashtagDTO */
    @Getter
    @Setter
    public static class FeedHashtagDTO {
        @Schema(description = "해시태그 ID", example = "1")
        private Long id;

        @Schema(description = "해시태그 이름", example = "#design")
        private String hashtag;

        public FeedHashtagDTO() {}

        public FeedHashtagDTO(Hashtag hashtag) {
            this.id = hashtag.getId();
            this.hashtag = hashtag.getHashtag();
        }
    }
}