package spotlight.spotlight_ver2.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import spotlight.spotlight_ver2.entity.ProjectRole;

@Getter
@Setter
@Schema(description = "프로젝트 역할 정보 DTO")
public class ProjectRoleDTO {

    @Schema(description = "프로젝트 역할 ID")
    private Long id;

    @Schema(description = "학생 ID")
    private Long userId;

    @Schema(description = "프로젝트 ID")
    private Long projectId;

    @Schema(description = "역할")
    private String role;

    @Schema(description = "초대 수락 여부")
    private boolean accepted;

    public ProjectRoleDTO() {}

    public ProjectRoleDTO(ProjectRole projectRole) {
        this.id = projectRole.getId();
        this.userId = (projectRole.getStudent() != null) ? projectRole.getStudent().getUserId() : null;
        this.projectId = (projectRole.getProject() != null) ? projectRole.getProject().getId() : null;
        this.role = projectRole.getRole();
        this.accepted = projectRole.isAccepted();
    }

    // 개별 필드를 인자로 받는 생성자
    public ProjectRoleDTO(Long id, Long userId, Long projectId, String role, boolean accepted) {
        this.id = id;
        this.userId = userId;
        this.projectId = projectId;
        this.role = role;
        this.accepted = accepted;
    }
}