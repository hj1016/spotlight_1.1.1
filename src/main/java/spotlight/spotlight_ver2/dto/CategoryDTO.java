package spotlight.spotlight_ver2.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.HashSet;
import java.util.Set;

@Schema(description = "카테고리 DTO")
public class CategoryDTO {

    @Schema(description = "카테고리 ID", example = "9")
    private Long id;

    @Schema(description = "카테고리 이름", example = "전기/전자")
    private String name;

    @Schema(description = "부모 카테고리 이름", example = "공학")
    private CategoryDTO parent;

    @Schema(description = "피드 정보", implementation = FeedDTO.class)
    private Set<FeedDTO> feeds = new HashSet<>();

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