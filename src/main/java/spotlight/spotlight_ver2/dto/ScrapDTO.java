package spotlight.spotlight_ver2.dto;

public class ScrapDTO {
    private Long scrapId;
    private UserDTO user;
    private FeedDTO feed;
    private StageDTO stageId;
    private UserDTO scrappedUser;

    // 기본 생성자
    public ScrapDTO() {}

    // 매개변수를 받는 생성자
    public ScrapDTO(Long scrapId, UserDTO user, FeedDTO feed, StageDTO stageId, UserDTO scrappedUser) {
        this.scrapId = scrapId;
        this.user = user;
        this.feed = feed;
        this.stageId = stageId;
        this.scrappedUser = scrappedUser;
    }

    // Getters and Setters
    public Long getScrapId() {
        return scrapId;
    }

    public void setScrapId(Long scrapId) {
        this.scrapId = scrapId;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    public FeedDTO getFeed() {
        return feed;
    }

    public void setFeed(FeedDTO feed) {
        this.feed = feed;
    }

    public StageDTO getStageId() {
        return stageId;
    }

    public void setStageId(StageDTO stageId) {
        this.stageId = stageId;
    }

    public UserDTO getScrappedUser() {
        return scrappedUser;
    }

    public void setScrappedUser(UserDTO scrappedUser) {
        this.scrappedUser = scrappedUser;
    }
}