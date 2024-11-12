package spotlight.spotlight_ver2.dto;

public class FeedRecommendationDTO {
    private Long feedId;
    private String title;
    private String thumbnailImage;

    // 생성자
    public FeedRecommendationDTO(Long feedId, String title, String thumbnailImage) {
        this.feedId = feedId;
        this.title = title;
        this.thumbnailImage = thumbnailImage;
    }

    // Getter와 Setter
    public Long getFeedId() {
        return feedId;
    }

    public void setFeedId(Long feedId) {
        this.feedId = feedId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getThumbnailImage() {
        return thumbnailImage;
    }

    public void setThumbnailImage(String thumbnailImage) {
        this.thumbnailImage = thumbnailImage;
    }
}