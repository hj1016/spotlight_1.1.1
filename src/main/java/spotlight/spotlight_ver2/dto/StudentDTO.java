package spotlight.spotlight_ver2.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;
import java.util.Set;

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
    private String enrollmentCertification;

    @Schema(description = "사용자 정보", implementation = UserDTO.class)
    private UserDTO user;

    @Schema(description = "제안서 목록", implementation = ProposalDTO.class)
    private List<ProposalDTO> proposals;

    @Schema(description = "프로젝트 역할 목록", implementation = ProjectRoleDTO.class)
    private Set<ProjectRoleDTO> projectRoles;

    // 기본 생성자
    public StudentDTO() {}

    // 매개변수를 받는 생성자
    public StudentDTO(Long userId, String school, String major, String portfolioImage, String enrollmentCertification, UserDTO user, List<ProposalDTO> proposals, Set<ProjectRoleDTO> projectRoles) {
        this.userId = userId;
        this.school = school;
        this.major = major;
        this.portfolioImage = portfolioImage;
        this.enrollmentCertification = enrollmentCertification;
        this.user = user;
        this.proposals = proposals;
        this.projectRoles = projectRoles;
    }

    // Getters and Setters
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public String getPortfolioImage() { return portfolioImage; }

    public void setPortfolioImage(String portfolioImage) { this.portfolioImage = portfolioImage; }

    public String getEnrollmentCertification() { return enrollmentCertification; }

    public void setEnrollmentCertification(String enrollmentCertification) { this.enrollmentCertification = enrollmentCertification; }

    public UserDTO getUser() { return user; }

    public void setUser(UserDTO user) { this.user = user; }

    public List<ProposalDTO> getProposals() { return proposals; }

    public void setProposals(List<ProposalDTO> proposals) { this.proposals = proposals; }

    public Set<ProjectRoleDTO> getProjectRoles() {
        return projectRoles;
    }

    public void setProjectRoles(Set<ProjectRoleDTO> projectRoles) {
        this.projectRoles = projectRoles;
    }
}