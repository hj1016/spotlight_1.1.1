package spotlight.spotlight_ver2.dto;

import io.swagger.v3.oas.annotations.media.Schema;

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

    public ProjectRoleDTO(Long id, Long userId, Long projectId, String role, boolean accepted) {
        this.id = id;
        this.userId = userId;
        this.projectId = projectId;
        this.role = role;
        this.accepted = accepted;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public boolean isAccepted() {
        return accepted;
    }

    public void setAccepted(boolean accepted) {
        this.accepted = accepted;
    }
}