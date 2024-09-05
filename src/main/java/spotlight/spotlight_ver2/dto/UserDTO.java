package spotlight.spotlight_ver2.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.List;

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

    @Schema(description = "계정 생성 일자", example = "2024-09-06T12:00:00")
    private LocalDateTime createdDate;

    @Schema(description = "계정 수정 일자", example = "2024-09-06T12:00:00")
    private LocalDateTime updatedDate;

    @Schema(description = "샤용자가 사용한 해시태그 목록", implementation = HashtagDTO.class)
    private List<HashtagDTO> hashtags;

    @Schema(description = "사용자가 작성한 피드 목록", implementation = FeedDTO.class)
    private List<FeedDTO> feeds;

    @Schema(description = "사용자가 스크랩한 피드 목록", implementation = ScrapDTO.class)
    private List<ScrapDTO> scraps;

    // 기본 생성자
    public UserDTO() {}

    // 매개변수를 받는 생성자
    public UserDTO(Long id, String username, String email, String name, String profileImage,
                   LocalDateTime createdDate, LocalDateTime updatedDate,
                   List<HashtagDTO> hashtags, List<FeedDTO> feeds, List<ScrapDTO> scraps) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.name = name;
        this.profileImage = profileImage;
        this.createdDate = createdDate;
        this.updatedDate = updatedDate;
        this.hashtags = hashtags;
        this.feeds = feeds;
        this.scraps = scraps;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public LocalDateTime getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(LocalDateTime updatedDate) {
        this.updatedDate = updatedDate;
    }

    public List<HashtagDTO> getHashtags() {
        return hashtags;
    }

    public void setHashtags(List<HashtagDTO> hashtags) {
        this.hashtags = hashtags;
    }

    public List<FeedDTO> getFeeds() {
        return feeds;
    }

    public void setFeeds(List<FeedDTO> feeds) {
        this.feeds = feeds;
    }

    public List<ScrapDTO> getScraps() {
        return scraps;
    }

    public void setScraps(List<ScrapDTO> scraps) {
        this.scraps = scraps;
    }
}