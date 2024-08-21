package spotlight.spotlight_ver2.dto;

import java.sql.Timestamp;
import java.util.Set;

public class FeedDTO {
    private Long feedId;
    private String title;
    private String thiumbnailImage;
    private String feedImages;
    private String content;
    private Integer scrap;
    private Integer hitsUser;
    private Integer hitsRecruiter;
    private CategoryDTO category;
    private Timestamp createdDate;
    private Timestamp modifiedDate;
    private UserDTO user;
    private ExhibitionDTO exhibition;
    private ProjectDTO project;
    private Set<HashtagDTO> hashtags;

    // 기본 생성자
    public FeedDTO() {}

    // 매개변수를 받는 생성자
    public FeedDTO(Long feedId, String title, String thiumbnailImage, String feedImages, String content, Integer scrap,
                   Integer hitsUser, Integer hitsRecruiter, CategoryDTO category, Timestamp createdDate, Timestamp modifiedDate,
                   UserDTO user, ExhibitionDTO exhibition, ProjectDTO project, Set<HashtagDTO> hashtags) {
        this.feedId = feedId;
        this.title = title;
        this.thiumbnailImage = thiumbnailImage;
        this.feedImages = feedImages;
        this.content = content;
        this.scrap = scrap;
        this.hitsUser = hitsUser;
        this.hitsRecruiter = hitsRecruiter;
        this.category = category;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
        this.user = user;
        this.exhibition = exhibition;
        this.project = project;
        this.hashtags = hashtags;
    }

    // Getters and Setters
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

    public String getThiumbnailImage() {
        return thiumbnailImage;
    }

    public void setThiumbnailImage(String thiumbnailImage) {
        this.thiumbnailImage = thiumbnailImage;
    }

    public String getFeedImages() {
        return feedImages;
    }

    public void setFeedImages(String feedImages) {
        this.feedImages = feedImages;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getScrap() {
        return scrap;
    }

    public void setScrap(Integer scrap) {
        this.scrap = scrap;
    }

    public Integer getHitsUser() {
        return hitsUser;
    }

    public void setHitsUser(Integer hitsUser) {
        this.hitsUser = hitsUser;
    }

    public Integer getHitsRecruiter() {
        return hitsRecruiter;
    }

    public void setHitsRecruiter(Integer hitsRecruiter) {
        this.hitsRecruiter = hitsRecruiter;
    }

    public CategoryDTO getCategory() {
        return category;
    }

    public void setCategory(CategoryDTO category) {
        this.category = category;
    }

    public Timestamp getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Timestamp createdDate) {
        this.createdDate = createdDate;
    }

    public Timestamp getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(Timestamp modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    public ExhibitionDTO getExhibition() {
        return exhibition;
    }

    public void setExhibition(ExhibitionDTO exhibition) {
        this.exhibition = exhibition;
    }

    public ProjectDTO getProject() {
        return project;
    }

    public void setProject(ProjectDTO project) {
        this.project = project;
    }

    public Set<HashtagDTO> getHashtags() {
        return hashtags;
    }

    public void setHashtags(Set<HashtagDTO> hashtags) {
        this.hashtags = hashtags;
    }
}