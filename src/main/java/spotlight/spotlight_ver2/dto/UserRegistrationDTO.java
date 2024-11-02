package spotlight.spotlight_ver2.dto;

import lombok.Data;
import spotlight.spotlight_ver2.enums.Role;

@Data
public class UserRegistrationDTO {
    private String email;
    private String username;
    private String password;
    private String name;
    private Role role;
    private String school;
    private String major;
    private String company;
    private String studentCertification;
    private String recruiterCertification;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
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

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getStudentCertification() {
        return studentCertification;
    }

    public void setStudentCertification(String studentCertification) {
        this.studentCertification = studentCertification;
    }

    public String getRecruiterCertification() {
        return recruiterCertification;
    }

    public void setRecruiterCertification(String recruiterCertification) {
        this.recruiterCertification = recruiterCertification;
    }
}
