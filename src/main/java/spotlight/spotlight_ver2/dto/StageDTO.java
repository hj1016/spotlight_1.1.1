package spotlight.spotlight_ver2.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "스테이지 DTO")
public class StageDTO {

    @Schema(description = "스테이지 ID", example = "1")
    private Long id;

    // 기본 생성자
    public StageDTO() {}

    // 매개변수를 받는 생성자
    public StageDTO(Long id) {
        this.id = id;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}