package spotlight.spotlight_ver2.entity;

import jakarta.persistence.*;

@Entity
public class Portfolio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long portfolioId;

    @ManyToOne
    private User user;

    @Column(length = 2000)
    private String portfolioImages;

    public Portfolio() {
    }

    public Portfolio(User user, String portfolioImages) {
        this.user = user;
        this.portfolioImages = portfolioImages;
    }

    public long getPortfolioId() {
        return portfolioId;
    }

    public void setPortfolioId(long portfolioId) {
        this.portfolioId = portfolioId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getPortfolioImages() {
        return portfolioImages;
    }

    public void setPortfolioImages(String portfolioImages) {
        this.portfolioImages = portfolioImages;
    }
}
