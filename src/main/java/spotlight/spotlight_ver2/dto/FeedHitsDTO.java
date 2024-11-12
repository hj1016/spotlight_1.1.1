package spotlight.spotlight_ver2.dto;

public class FeedHitsDTO {
    private Long feedId;
    private int hitsUser;
    private int hitsRecruiter;

    // 기본 생성자
    public FeedHitsDTO() {}

    // 모든 필드를 초기화하는 생성자
    public FeedHitsDTO(Long feedId, int hitsUser, int hitsRecruiter) {
        this.feedId = feedId;
        this.hitsUser = hitsUser;
        this.hitsRecruiter = hitsRecruiter;
    }

    // Getter & Setter
    public Long getFeedId() {
        return feedId;
    }

    public void setFeedId(Long feedId) {
        this.feedId = feedId;
    }

    public int getHitsUser() {
        return hitsUser;
    }

    public void setHitsUser(int hitsUser) {
        this.hitsUser = hitsUser;
    }

    public int getHitsRecruiter() {
        return hitsRecruiter;
    }

    public void setHitsRecruiter(int hitsRecruiter) {
        this.hitsRecruiter = hitsRecruiter;
    }
}