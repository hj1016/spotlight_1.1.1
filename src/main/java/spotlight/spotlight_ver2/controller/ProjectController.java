package spotlight.spotlight_ver2.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import spotlight.spotlight_ver2.dto.ProjectRoleDTO;
import spotlight.spotlight_ver2.entity.ProjectRole;
import spotlight.spotlight_ver2.exception.ProjectNotFoundException;
import spotlight.spotlight_ver2.exception.ProjectRoleNotFoundException;
import spotlight.spotlight_ver2.exception.StudentNotFoundException;
import spotlight.spotlight_ver2.exception.InvalidRoleException;
import spotlight.spotlight_ver2.service.ProjectService;
import spotlight.spotlight_ver2.service.UserService;

@Tag(name="프로젝트 API", description = "프로젝트 생성자가 팀원을 초대하는 API 입니다.")
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
            @ApiResponse(responseCode = "404", description = "프로젝트 또는 학생을 찾을 수 없음"),
            @ApiResponse(responseCode = "400", description = "유효하지 않은 역할"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @PostMapping("/{projectId}/invite")
    public ResponseEntity<ProjectRoleDTO> inviteTeamMember(@PathVariable Long projectId,
                                                           @RequestParam Long studentId,
                                                           @RequestParam String role) {
        try {
            ProjectRole projectRole = projectService.inviteTeamMember(projectId, studentId, role);
            ProjectRoleDTO dto = new ProjectRoleDTO(projectRole.getId(), projectRole.getStudent().getUser().getId(),
                    projectRole.getProject().getId(), projectRole.getRole(), projectRole.isAccepted());
            return ResponseEntity.ok(dto);
        } catch (ProjectNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (StudentNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (InvalidRoleException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @Operation(summary = "초대 수락", description = "프로젝트 초대를 수락합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "초대 수락 성공"),
            @ApiResponse(responseCode = "404", description = "프로젝트 역할을 찾을 수 없음"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @PostMapping("/accept/{projectRoleId}")
    public ResponseEntity<Void> acceptInvitation(@PathVariable Long projectRoleId) {
        try {
            projectService.acceptInvitation(projectRoleId);
            return ResponseEntity.ok().build();
        } catch (ProjectRoleNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}