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

    @Column(nullable = false)
    private String school;

    @Column(nullable = false)
    private String major;

    @Column(name = "pf_path")
    private String portfolioImage;

    @Column(name = "student_certificate", nullable = true)
    private String studentCertificate;

    @OneToOne
    @MapsId
    @JoinColumn(name = "user_id", nullable = false, referencedColumnName = "id")
    private User user;

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL)
    private List<Proposal> proposals = new ArrayList<>();

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ProjectRole> projectRoles = new HashSet<>();

    public Long getUserId() { return userId; }

    public void setUserId(Long userId) {
        this.userId = userId;
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

    public String getPortfolioImage() {
        return portfolioImage;
    }

    public void setPortfolioImage(String portfolioImage) {
        this.portfolioImage = portfolioImage;
    }

    public String getStudentCertificate() { return studentCertificate; }

    public void setStudentCertificate(String studentCertificate) { this.studentCertificate = studentCertificate; }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Proposal> getProposals() {
        return proposals;
    }

    public void setProposals(List<Proposal> proposals) {
        this.proposals = proposals;
    }

    public Set<ProjectRole> getProjectRoles() { return projectRoles; }

    public void setProjectRoles(Set<ProjectRole> projectRoles) { this.projectRoles = projectRoles; }
}