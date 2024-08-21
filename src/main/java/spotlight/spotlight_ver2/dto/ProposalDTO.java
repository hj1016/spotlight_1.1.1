package spotlight.spotlight_ver2.dto;

import java.time.LocalDateTime;

public class ProposalDTO {
    private Long proposalId;
    private String job;
    private String contact;
    private String description;
    private LocalDateTime createdDate;
    private RecruiterDTO recruiter;
    private StudentDTO student;
    private String status;

    // 기본 생성자
    public ProposalDTO() {}

    // 매개변수를 받는 생성자
    public ProposalDTO(Long proposalId, String job, String contact, String description, LocalDateTime createdDate,
                       RecruiterDTO recruiter, StudentDTO student, String status) {
        this.proposalId = proposalId;
        this.job = job;
        this.contact = contact;
        this.description = description;
        this.createdDate = createdDate;
        this.recruiter = recruiter;
        this.student = student;
        this.status = status;
    }

    // Getters and Setters
    public Long getProposalId() {
        return proposalId;
    }

    public void setProposalId(Long proposalId) {
        this.proposalId = proposalId;
    }

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

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public RecruiterDTO getRecruiter() {
        return recruiter;
    }

    public void setRecruiter(RecruiterDTO recruiter) {
        this.recruiter = recruiter;
    }

    public StudentDTO getStudent() {
        return student;
    }

    public void setStudent(StudentDTO student) {
        this.student = student;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}