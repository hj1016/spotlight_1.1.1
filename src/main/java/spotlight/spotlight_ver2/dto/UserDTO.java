package spotlight.spotlight_ver2.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import spotlight.spotlight_ver2.entity.*;
import spotlight.spotlight_ver2.enums.Role;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@Schema(description = "사용자 정보 DTO")
public class UserDTO {

    @Schema(description = "사용자 ID", example = "1")
    private Long id;

    @Schema(description = "사용자 이름", example = "김학생")
    private String username;

    @Schema(description = "이메일 주소", example = "김학생@example.com")
    private String email;

    @Schema(description = "사용자 실명", example = "김학생")
    private String name;

    @Schema(description = "프로필 이미지 URL", example = "http://example.com/profile.jpg")
    private String profileImage;

    @Schema(description = "사용자 유형", example = "NORMAL")
    private Role role;

    @Schema(description = "계정 생성 일자", example = "2024-09-06T12:00:00")
    private LocalDateTime createdDate;

    @Schema(description = "계정 수정 일자", example = "2024-09-06T12:00:00")
    private LocalDateTime updatedDate;

    @Schema(description = "사용자가 학생일 경우의 정보", implementation = UserStudentDTO.class)
    private UserStudentDTO student;

    @Schema(description = "사용자가 인사 담당자일 경우의 정보", implementation = UserRecruiterDTO.class)
    private UserRecruiterDTO recruiter;

    @Schema(description = "사용자가 사용한 해시태그 목록", implementation = UserHashtagDTO.class)
    private List<UserHashtagDTO> hashtags;

    @Schema(description = "사용자가 작성한 피드 목록", implementation = UserFeedDTO.class)
    private List<UserFeedDTO> feeds;

    @Schema(description = "사용자가 스크랩한 피드 목록", implementation = UserScrapDTO.class)
    private List<UserScrapDTO> scraps;

    public UserDTO() {}

    /* User -> UserDTO */
    public UserDTO(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.name = user.getName();
        this.profileImage = user.getProfileImage();
        this.createdDate = user.getCreatedDate();
        this.updatedDate = user.getUpdatedDate();

        this.hashtags = (user.getHashtags() != null) ? user.getHashtags().stream()
                .map(UserHashtagDTO::new)
                .collect(Collectors.toList()) : List.of();

        this.feeds = (user.getFeeds() != null) ? user.getFeeds().stream()
                .map(UserFeedDTO::new)
                .collect(Collectors.toList()) : List.of();

        this.scraps = (user.getScraps() != null) ? user.getScraps().stream()
                .map(UserScrapDTO::new)
                .collect(Collectors.toList()) : List.of();
    }

    /* 참조할 StudentDTO */
    @Getter
    public static class UserStudentDTO {
        @Schema(description = "학교", example = "서울여자대학교")
        private String school;

        @Schema(description = "전공", example = "소프트웨어융합학과")
        private String major;

        public UserStudentDTO(Student student) {
            this.school = student.getSchool();
            this.major = student.getMajor();
        }
    }

    /* 참조할 RecruiterDTO */
    @Getter
    public static class UserRecruiterDTO {
        @Schema(description = "회사", example = "네이버")
        private String company;

        @Schema(description = "재직 증명서 URL", example = "http://example.com/certification.jpg")
        private String certification;

        public UserRecruiterDTO(Recruiter recruiter) {
            this.company = recruiter.getCompany();
            this.certification = recruiter.getRecruiterCertificate();
        }
    }

    /* 참조할 HashtagDTO */
    @Getter
    public static class UserHashtagDTO {
        @Schema(description = "해시태그 ID", example = "1")
        private Integer id;

        @Schema(description = "해시태그 이름", example = "#design")
        private String hashtag;

        public UserHashtagDTO(Hashtag hashtag) {
            this.id = hashtag.getId();
            this.hashtag = hashtag.getHashtag();
        }
    }

    /* 참조할 FeedDTO */
    @Getter
    public static class UserFeedDTO {
        @Schema(description = "피드 ID", example = "1")
        private Long feedId;

        @Schema(description = "피드 제목", example = "Feed Title")
        private String title;

        public UserFeedDTO(Feed feed) {
            this.feedId = feed.getFeedId();
            this.title = feed.getTitle();
        }
    }

    /* 참조할 ScrapDTO */
    @Getter
    @Setter
    public static class UserScrapDTO {
        @Schema(description = "스크랩 ID", example = "1")
        private Long scrapId;

        @Schema(description = "스크랩한 피드 제목", example = "Feed Title")
        private String feedTitle;

        @Schema(description = "단계 ID", example = "2")
        private Long stageId;

        @Schema(description = "스크랩된 사용자 이름", example = "김학생")
        private String scrappedUserName;

        public UserScrapDTO(Scrap scrap) {
            this.scrapId = scrap.getScrapId();
            this.feedTitle = (scrap.getFeed() != null) ? scrap.getFeed().getTitle() : null;
            this.stageId = (scrap.getStageId() != null) ? scrap.getStageId().getId() : null;
            this.scrappedUserName = (scrap.getScrappedUser() != null) ? scrap.getScrappedUser().getName() : null;
        }
    }
}