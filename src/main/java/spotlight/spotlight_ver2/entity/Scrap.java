package spotlight.spotlight_ver2.entity;

import jakarta.persistence.*;

@Entity
public class Scrap {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "scrap_id")
    private Long scrapId;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "feed_id", nullable = true)
    private Feed feed;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stage_id", nullable = true)
    private Stage stageId;

    @ManyToOne
    @JoinColumn(name = "scrapped_user_id", referencedColumnName = "id", nullable = true)
    private User scrappedUser;

    public Long getScrapId() {
        return scrapId;
    }

    public void setScrapId(Long scrapId) {
        this.scrapId = scrapId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Feed getFeed() {
        return feed;
    }

    public void setFeed(Feed feed) {
        this.feed = feed;
    }

    public Stage getStageId() {
        return stageId;
    }

    public void setStageId(Stage stageId) {
        this.stageId = stageId;
    }

    public User getScrappedUser() {
        return scrappedUser;
    }

    public void setScrappedUser(User scrappedUser) {
        this.scrappedUser = scrappedUser;
    }
}