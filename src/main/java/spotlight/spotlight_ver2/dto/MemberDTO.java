package spotlight.spotlight_ver2.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import spotlight.spotlight_ver2.entity.Student;

import java.util.List;

@Getter
@Setter
@Schema(description = "팀원 정보 DTO")
public class MemberDTO {

    @Schema(description = "팀원 이름", example = "김학생")
    private String name;

    @Schema(description = "학교 이름", example = "서울여자대학교")
    private String school;

    @Schema(description = "학과 이름", example = "소프트웨어융합학과")
    private String major;

    @Schema(description = "참여한 프로젝트(피드) 리스트", implementation = ProjectInfoDTO.class)
    private List<ProjectInfoDTO> projects; // 참여한 프로젝트 정보 리스트

    public MemberDTO() {}

    public MemberDTO(Student student, List<ProjectInfoDTO> projects) {
        this.name = student.getUser().getName();
        this.school = student.getSchool();
        this.major = student.getMajor();
        this.projects = projects; // 프로젝트 정보 리스트
    }

    @Getter
    @Setter
    @Schema(description = "프로젝트 정보 DTO")
    public static class ProjectInfoDTO {

        @Schema(description = "프로젝트(피드) 제목", example = "Graduation Project")
        private String title;

        @Schema(description = "피드 썸네일 이미지 URL", example = "http://example.com/project-thumbnail.jpg")
        private String thumbnailImage;

        @Schema(description = "피드 카테고리", example = "Software")
        private String category;

        @Schema(description = "학생의 역할", example = "Developer")
        private String role;

        public ProjectInfoDTO(String title, String thumbnailImage, String category, String role) {
            this.title = title;
            this.thumbnailImage = thumbnailImage;
            this.category = category;
            this.role = role;
        }
    }
}