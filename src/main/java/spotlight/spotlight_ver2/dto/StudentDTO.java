package spotlight.spotlight_ver2.dto;

import java.util.List;
import java.util.Set;

public class StudentDTO {
    private Long userId;
    private String school;
    private String major;
    private String portfolioImage;
    private Set<ProjectRoleDTO> projectRoles;
    private List<ProposalDTO> proposals;

    // 기본 생성자
    public StudentDTO() {}

    // 매개변수를 받는 생성자
    public StudentDTO(Long userId, String school, String major, String portfolioImage, Set<ProjectRoleDTO> projectRoles, List<ProposalDTO> proposals) {
        this.userId = userId;
        this.school = school;
        this.major = major;
        this.portfolioImage = portfolioImage;
        this.projectRoles = projectRoles;
        this.proposals = proposals;
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

    public String getPortfolioImage() {
        return portfolioImage;
    }

    public void setPortfolioImage(String portfolioImage) {
        this.portfolioImage = portfolioImage;
    }

    public Set<ProjectRoleDTO> getProjectRoles() {
        return projectRoles;
    }

    public void setProjectRoles(Set<ProjectRoleDTO> projectRoles) {
        this.projectRoles = projectRoles;
    }

    public List<ProposalDTO> getProposals() {
        return proposals;
    }

    public void setProposals(List<ProposalDTO> proposals) {
        this.proposals = proposals;
    }
}