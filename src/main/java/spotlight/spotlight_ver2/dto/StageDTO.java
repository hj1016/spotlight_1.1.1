package spotlight.spotlight_ver2.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import spotlight.spotlight_ver2.entity.Stage;

@Schema(description = "스테이지 DTO")
public class StageDTO {

    @Schema(description = "스테이지 ID", example = "1")
    private Long id;

    public StageDTO() {}

    public StageDTO(Stage stage) {
        if (stage != null) {
            this.id = stage.getId();
        } else {
            this.id = null;
        }
    }
}