package spotlight.spotlight_ver2.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "프로젝트 정보 DTO")
public class ProjectDTO {
    @Schema(description = "프로젝트 ID")
    private Long id;

    @Schema(description = "프로젝트 이름")
    private String name;

    @Schema(description = "프로젝트 생성자")
    private UserDTO creator;

    @Schema(description = "팀원 역할 리스트")
    private List<ProjectRoleDTO> projectRoles;

    // 기본 생성자
    public ProjectDTO() {}

    // 매개변수를 받는 생성자
    public ProjectDTO(Long id, String name, UserDTO creator, List<ProjectRoleDTO> projectRoles) {
        this.id = id;
        this.name = name;
        this.creator = creator;
        this.projectRoles = projectRoles;
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

    public UserDTO getCreator() {
        return creator;
    }

    public void setCreator(UserDTO creator) {
        this.creator = creator;
    }

    public List<ProjectRoleDTO> getProjectRoles() {
        return projectRoles;
    }

    public void setProjectRoles(List<ProjectRoleDTO> projectRoles) {
        this.projectRoles = projectRoles;
    }
}