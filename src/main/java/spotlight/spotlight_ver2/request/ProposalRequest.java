package spotlight.spotlight_ver2.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProposalRequest {
    private String job;
    private String contact;
    private String description;
    private Long studentId;

    // 기본 생성자
    public ProposalRequest() {}

    // 필드 초기화 생성자
    public ProposalRequest(String job, String contact, String description, Long studentId) {
        this.job = job;
        this.contact = contact;
        this.description = description;
        this.studentId = studentId;
    }

    // Getter and Setter
    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }
}
