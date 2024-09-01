package spotlight.spotlight_ver2.dto;

import java.util.List;
import java.util.Set;

public class StudentDTO {
    private Long userId;
    private String school;
    private String major;
    private String portfolioImage;
    private String enrollmentCertification;
    private UserDTO user;
    private List<ProposalDTO> proposals;
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