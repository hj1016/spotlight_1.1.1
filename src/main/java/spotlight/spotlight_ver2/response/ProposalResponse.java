package spotlight.spotlight_ver2.response;

import lombok.Getter;
import lombok.Setter;
import spotlight.spotlight_ver2.dto.ProposalDTO;

@Getter
@Setter
public class ProposalResponse {
    private Long proposalId;
    private String job;
    private String contact;
    private String description;
    private String createdDate;
    private String status;
    private ProposalDTO.ProposalRecruiterDTO recruiter;
    private ProposalDTO.ProposalStudentDTO student;

    // 기본 생성자
    public ProposalResponse() {}

    // Getter and Setter
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

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ProposalDTO.ProposalRecruiterDTO getRecruiter() {
        return recruiter;
    }

    public void setRecruiter(ProposalDTO.ProposalRecruiterDTO recruiter) {
        this.recruiter = recruiter;
    }

    public ProposalDTO.ProposalStudentDTO getStudent() {
        return student;
    }

    public void setStudent(ProposalDTO.ProposalStudentDTO student) {
        this.student = student;
    }
}
