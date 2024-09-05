package spotlight.spotlight_ver2.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.sql.Timestamp;
import java.util.Set;

@Schema(description = "피드 DTO")
public class FeedDTO {

    @Schema(description = "피드 ID", example = "1")
    private Long feedId;

    @Schema(description = "피드 제목", example = "Feed Title")
    private String title;

    @Schema(description = "썸네일 이미지 URL", example = "http://example.com/thumbnail.jpg")
    private String thumbnailImage;

    @Schema(description = "피드 이미지 URLemf", example = "[\"http://example.com/image1.jpg\", \"http://example.com/image2.jpg\"]")
    private String feedImages;

    @Schema(description = "피드 내용", example = "This is the content of the feed.")
    private String content;

    @Schema(description = "스크랩 수", example = "123")
    private Integer scrap;

    @Schema(description = "사용자 조회 수", example = "456")
    private Integer hitsUser;

    @Schema(description = "리크루터 조회 수", example = "789")
    private Integer hitsRecruiter;

    @Schema(description = "카테고리", implementation = CategoryDTO.class)
    private CategoryDTO category;

    @Schema(description = "생성 일자", example = "2024-09-06T12:00:00Z")
    private Timestamp createdDate;

    @Schema(description = "수정 일자", example = "2024-09-06T12:00:00Z")
    private Timestamp modifiedDate;

    @Schema(description = "사용자 정보", implementation = UserDTO.class)
    private UserDTO user;

    @Schema(description = "전시 정보", implementation = ExhibitionDTO.class)
    private ExhibitionDTO exhibition;

    @Schema(description = "프로젝트 정보", implementation = ProjectDTO.class)
    private ProjectDTO project;

    @Schema(description = "해시태그 집합", implementation = HashtagDTO.class)
    private Set<HashtagDTO> hashtags;

    // 기본 생성자
    public FeedDTO() {}

    // 매개변수를 받는 생성자
    public FeedDTO(Long feedId, String title, String thumbnailImage, String feedImages, String content, Integer scrap,
                   Integer hitsUser, Integer hitsRecruiter, CategoryDTO category, Timestamp createdDate, Timestamp modifiedDate,
                   UserDTO user, ExhibitionDTO exhibition, ProjectDTO project, Set<HashtagDTO> hashtags) {
        this.feedId = feedId;
        this.title = title;
        this.thumbnailImage = thumbnailImage;
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

    public String getThumbnailImage() {
        return thumbnailImage;
    }

    public void setThumbnailImage(String thiumbnailImage) {
        this.thumbnailImage = thiumbnailImage;
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