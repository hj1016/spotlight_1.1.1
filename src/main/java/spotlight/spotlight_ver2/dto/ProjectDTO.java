package spotlight.spotlight_ver2.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import spotlight.spotlight_ver2.entity.Feed;
import spotlight.spotlight_ver2.entity.Project;
import spotlight.spotlight_ver2.entity.ProjectRole;
import spotlight.spotlight_ver2.entity.User;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@Schema(description = "프로젝트 정보 DTO")
public class ProjectDTO {

    @Schema(description = "프로젝트 ID")
    private Long id;

    @Schema(description = "프로젝트 이름")
    private String name;

    @Schema(description = "프로젝트 생성자")
    private ProjectUserDTO creator;

    @Schema(description = "프로젝트와 관련된 피드들")
    private Set<ProjectFeedDTO> feeds;

    @Schema(description = "팀원 역할 리스트", implementation = ProjectProjectRoleDTO.class)
    private List<ProjectProjectRoleDTO> projectRoles;

    public ProjectDTO() {}

    public ProjectDTO(Project project) {
        this.id = project.getId();
        this.name = project.getName();

        if (project.getCreator() != null) {
            this.creator = new ProjectUserDTO(project.getCreator());
        }

        // projectRoles가 null이 아닌지 체크
        this.projectRoles = (project.getProjectRoles() != null) ?
                project.getProjectRoles().values().stream()
                        .map(ProjectProjectRoleDTO::new)
                        .collect(Collectors.toList()) :
                List.of();

        // feeds가 null이 아닌지 체크하여 초기화
        this.feeds = (project.getFeeds() != null) ?
                project.getFeeds().stream()
                        .map(ProjectFeedDTO::new)
                        .collect(Collectors.toSet()) :
                Set.of();
    }

    @Getter
    @Setter
    public static class ProjectUserDTO {
        @Schema(description = "사용자 ID", example = "1")
        private Long id;

        @Schema(description = "사용자 실명", example = "김학생")
        private String name;

        public ProjectUserDTO() {}

        public ProjectUserDTO(User user){
            this.id = user.getId();
            this.name = user.getName();
        }
    }

    @Getter
    @Setter
    public static class ProjectFeedDTO {
        @Schema(description = "피드 ID")
        private Long feedId;

        @Schema(description = "피드 제목")
        private String title;

        public ProjectFeedDTO() {}

        public ProjectFeedDTO(Feed feed) {
            this.feedId = feed.getFeedId();
            this.title = feed.getTitle();
        }
    }

    @Getter
    @Setter
    public static class ProjectProjectRoleDTO {  // static 추가

        @Schema(description = "프로젝트 역할 ID")
        private Long id;

        @Schema(description = "역할")
        private String role;

        @Schema(description = "초대 수락 여부")
        private boolean accepted;

        public ProjectProjectRoleDTO() {}

        public ProjectProjectRoleDTO(ProjectRole projectRole) {
            this.id = projectRole.getId();
            this.role = projectRole.getRole();
            this.accepted = projectRole.isAccepted();
        }
    }
}