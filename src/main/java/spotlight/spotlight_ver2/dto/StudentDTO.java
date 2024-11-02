package spotlight.spotlight_ver2.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import spotlight.spotlight_ver2.entity.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@Schema(description = "학생 DTO")
public class StudentDTO {

    @Schema(description = "사용자 ID", example = "1")
    private Long userId;

    @Schema(description = "학교", example = "서울여자대학교")
    private String school;

    @Schema(description = "전공", example = "소프트웨어융합학과")
    private String major;

    @Schema(description = "포트폴리오 이미지 URL", example = "http://example.com/portfolio.jpg")
    private String portfolioImage;

    @Schema(description = "재학 증명서 URL", example = "http://example.com/enrollment-cert.jpg")
    private String studentCertificate;

    @Schema(description = "사용자 정보", implementation = StudentUserDTO.class)
    private StudentUserDTO user;

    @Schema(description = "제안서 목록", implementation = StudentProposalDTO.class)
    private List<StudentProposalDTO> proposals;

    @Schema(description = "프로젝트 역할 목록", implementation = StudentProjectRoleDTO.class)
    private Set<StudentProjectRoleDTO> projectRoles;

    public StudentDTO() {}

    public StudentDTO(Student student) {
        this.userId = student.getUserId();
        this.school = student.getSchool();
        this.major = student.getMajor();
        this.portfolioImage = student.getPortfolioImage();
        this.studentCertificate = student.getStudentCertificate();

        // 사용자 정보 초기화
        this.user = (student.getUser() != null) ? new StudentUserDTO(student.getUser()) : null;

        // 제안서 목록 초기화
        this.proposals = (student.getProposals() != null) ? student.getProposals().stream()
                .map(StudentProposalDTO::new)
                .collect(Collectors.toList()) : List.of();

        // 프로젝트 역할 목록 초기화
        this.projectRoles = (student.getProjectRoles() != null) ? student.getProjectRoles().stream()
                .map(StudentProjectRoleDTO::new)
                .collect(Collectors.toSet()) : Set.of();
    }

    @Getter
    public static class StudentUserDTO {
        @Schema(description = "사용자 ID", example = "1")
        private Long id;

        @Schema(description = "사용자 이름", example = "김학생")
        private String username;

        @Schema(description = "이메일 주소", example = "김학생@example.com")
        private String email;

        @Schema(description = "사용자 실명", example = "김학생")
        private String name;

        public StudentUserDTO() {}

        public StudentUserDTO(User user) {
            this.id = user.getId();
            this.username = user.getUsername();
            this.email = user.getEmail();
            this.name = user.getName();
        }
    }

    @Getter
    public static class StudentProposalDTO {
        @Schema(description = "제안서 ID", example = "1")
        private Long proposalId;

        @Schema(description = "제안서 직무", example = "Software Developer")
        private String job;

        @Schema(description = "연락처", example = "hisoka@example.com")
        private String contact;

        @Schema(description = "제안서 설명", example = "This is a proposal for a software development position.")
        private String description;

        @Schema(description = "제안서 생성 일자", example = "2024-09-06T12:00:00")
        private LocalDateTime createdDate;

        @Schema(description = "제안서 상태", example = "Pending")
        private String status;

        @Schema(description = "리크루터 정보", implementation = RecruiterDTO.class)
        private RecruiterDTO recruiter;

        public StudentProposalDTO() {}

        public StudentProposalDTO(Proposal proposal) {
            this.proposalId = proposal.getProposalId();
            this.job = proposal.getJob();
            this.contact = proposal.getContact();
            this.description = proposal.getDescription();
            this.createdDate = proposal.getCreatedDate();
            this.status = proposal.getStatus();

            // 리크루터 정보 초기화
            this.recruiter = (proposal.getRecruiter() != null) ? new RecruiterDTO(proposal.getRecruiter()) : null;
        }
    }

    @Getter
    public static class StudentProjectRoleDTO {
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

        public StudentProjectRoleDTO() {}

        public StudentProjectRoleDTO(ProjectRole projectRole) {
            this.id = projectRole.getId();
            this.userId = (projectRole.getStudent() != null) ? projectRole.getStudent().getUserId() : null;
            this.projectId = (projectRole.getProject() != null) ? projectRole.getProject().getId() : null;
            this.role = projectRole.getRole();
            this.accepted = projectRole.isAccepted();
        }
    }
}