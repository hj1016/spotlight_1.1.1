package spotlight.spotlight_ver2.dto;

import java.util.Set;

public class CategoryDTO {
    private Long id;
    private String name;
    private CategoryDTO parent;
    private Set<FeedDTO> feeds;

    // 기본 생성자
    public CategoryDTO() {}

    // 매개변수를 받는 생성자
    public CategoryDTO(Long id, String name, CategoryDTO parent, Set<FeedDTO> feeds) {
        this.id = id;
        this.name = name;
        this.parent = parent;
        this.feeds = feeds;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public CategoryDTO getParent() {
        return parent;
    }

    public void setParent(CategoryDTO parent) {
        this.parent = parent;
    }

    public Set<FeedDTO> getFeeds() {
        return feeds;
    }

    public void setFeeds(Set<FeedDTO> feeds) {
        this.feeds = feeds;
    }
}