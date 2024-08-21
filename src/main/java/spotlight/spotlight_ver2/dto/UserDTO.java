package spotlight.spotlight_ver2.dto;

import java.time.LocalDateTime;
import java.util.List;

public class UserDTO {
    private Long id;
    private String username;
    private String email;
    private String name;
    private String profileImage;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
    private List<HashtagDTO> hashtags;
    private List<FeedDTO> feeds;
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