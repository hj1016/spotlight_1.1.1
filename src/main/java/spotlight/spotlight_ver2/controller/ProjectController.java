package spotlight.spotlight_ver2.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import spotlight.spotlight_ver2.dto.ProjectRoleDTO;
import spotlight.spotlight_ver2.entity.ProjectRole;
import spotlight.spotlight_ver2.service.ProjectService;
import spotlight.spotlight_ver2.service.UserService;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {
    private final ProjectService projectService;
    private final UserService userService;

    public ProjectController(ProjectService projectService, UserService userService) {
        this.projectService = projectService;
        this.userService = userService;
    }

    @Operation(summary = "팀원 초대", description = "프로젝트에 팀원을 초대합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "팀원 초대 성공"),
            @ApiResponse(responseCode = "404", description = "프로젝트 또는 학생을 찾을 수 없음")
    })
    @PostMapping("/{projectId}/invite")
    public ResponseEntity<ProjectRoleDTO> inviteTeamMember(
            @PathVariable @Parameter(description = "프로젝트 ID") Long projectId,
            @RequestParam @Parameter(description = "학생 ID") Long studentId,
            @RequestParam @Parameter(description = "팀원 역할") String role) {
        ProjectRole projectRole = projectService.inviteTeamMember(projectId, studentId, role);
        ProjectRoleDTO dto = new ProjectRoleDTO(projectRole.getId(), projectRole.getStudent().getUser().getId(),
                projectRole.getProject().getId(), projectRole.getRole(), projectRole.isAccepted());
        return ResponseEntity.ok(dto);
    }

    @Operation(summary = "팀원 초대 수락", description = "팀원이 초대를 수락합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "초대 수락 성공"),
            @ApiResponse(responseCode = "404", description = "프로젝트 역할을 찾을 수 없음")
    })
    @PostMapping("/accept/{projectRoleId}")
    public ResponseEntity<Void> acceptInvitation(
            @PathVariable @Parameter(description = "ProjectRole ID") Long projectRoleId) {
        projectService.acceptInvitation(projectRoleId);
        return ResponseEntity.ok().build();
    }
}