package spotlight.spotlight_ver2.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import spotlight.spotlight_ver2.entity.Category;
import spotlight.spotlight_ver2.entity.Feed;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@Schema(description = "카테고리 DTO")
public class CategoryDTO {

    @Schema(description = "카테고리 ID", example = "9")
    private Long id;

    @Schema(description = "카테고리 이름", example = "전기/전자")
    private String name;

    @Schema(description = "부모 카테고리 ID", example = "1")
    private Long parentId;

    @Schema(description = "피드 정보", implementation = CategoryFeedDTO.class)
    private Set<CategoryFeedDTO> feeds = new HashSet<>();

    public CategoryDTO() {}

    public CategoryDTO(Category category) {
        this.id = category.getId();
        this.name = category.getName();
        this.parentId = (category.getParent() != null) ? category.getParent().getId() : null;

        this.feeds = category.getFeeds().stream()
                .map(CategoryFeedDTO::new)
                .collect(Collectors.toSet());
    }

    @Getter
    public static class CategoryFeedDTO {
        @Schema(description = "피드 ID", example = "1")
        private Long feedId;

        @Schema(description = "피드 제목", example = "Feed Title")
        private String title;

        public CategoryFeedDTO() {}

        public CategoryFeedDTO(Feed feed) {
            this.feedId = feed.getFeedId();
            this.title = feed.getTitle();
        }
    }
}