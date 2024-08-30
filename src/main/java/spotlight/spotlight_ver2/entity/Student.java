package spotlight.spotlight_ver2.entity;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
public class Student {
    @Id
    private Long userId;

    @OneToOne
    @MapsId
    @JoinColumn(name = "user_id", nullable = false, referencedColumnName = "id")
    private User user;

    @Column(nullable = false)
    private String school;

    @Column(nullable = false)
    private String major;

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ProjectRole> projectRoles = new HashSet<>();

    @Column(name = "pf_path")
    private String portfolioImage;

    @Column
    private String enrollmentCertification;

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL)
    private List<Proposal> proposals = new ArrayList<>();

    public Long getUserId() { return userId; }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public String getMajor() { return major; }

    public void setMajor(String major) {
        this.major = major;
    }

    public Set<ProjectRole> getProjectRoles() { return projectRoles; }

    public void setProjectRoles(Set<ProjectRole> projectRoles) { this.projectRoles = projectRoles; }

    public String getPortfolioImage() {
        return portfolioImage;
    }

    public void setPortfolioImage(String portfolioImage) {
        this.portfolioImage = portfolioImage;
    }

    public String getEnrollmentCertification() { return enrollmentCertification; }

    public void setEnrollmentCertification(String enrollmentCertification) { this.enrollmentCertification = enrollmentCertification; }

    public List<Proposal> getProposals() {
        return proposals;
    }

    public void setProposals(List<Proposal> proposals) {
        this.proposals = proposals;
    }
}
