package spotlight.spotlight_ver2.entity;

import jakarta.persistence.*;

@Entity
public class Recruiter {
    @Id
    private Long userId;

    @OneToOne
    @MapsId
    @JoinColumn(name = "user_id", nullable = false, referencedColumnName = "id")
    private User user;

    @Column(name = "company", nullable = false)
    private String company;

    @Column(name = "recruiter_certificate", nullable = true)
    private String recruiterCertificate;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getRecruiterCertificate() { return recruiterCertificate; }

    public void setRecruiterCertificate(String recruiterCertificate) { this.recruiterCertificate = recruiterCertificate; }
}
