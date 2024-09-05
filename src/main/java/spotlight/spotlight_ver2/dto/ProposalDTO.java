package spotlight.spotlight_ver2.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "제안서 정보 DTO")
public class ProposalDTO {

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

    @Schema(description = "리크루터 정보", implementation = RecruiterDTO.class)
    private RecruiterDTO recruiter;

    @Schema(description = "학생 정보", implementation = StudentDTO.class)
    private StudentDTO student;

    @Schema(description = "제안서 상태", example = "Pending")
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