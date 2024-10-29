package spotlight.spotlight_ver2.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import spotlight.spotlight_ver2.entity.Proposal;
import spotlight.spotlight_ver2.entity.Recruiter;
import spotlight.spotlight_ver2.entity.Student;

import java.time.LocalDateTime;

@Getter
@Setter
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

    @Schema(description = "제안서 상태", example = "Pending")
    private String status;

    @Schema(description = "리크루터 정보", implementation = ProposalRecruiterDTO.class)
    private ProposalRecruiterDTO recruiter;

    @Schema(description = "학생 정보", implementation = ProposalStudentDTO.class)
    private ProposalStudentDTO student;

    public ProposalDTO() {}

    public ProposalDTO(Proposal proposal) {
        this.proposalId = proposal.getProposalId();
        this.job = proposal.getJob();
        this.contact = proposal.getContact();
        this.description = proposal.getDescription();
        this.createdDate = proposal.getCreatedDate();
        this.status = proposal.getStatus();

        // 리크루터 정보 초기화
        this.recruiter = (proposal.getRecruiter() != null) ? new ProposalRecruiterDTO(proposal.getRecruiter()) : null;

        // 학생 정보 초기화
        this.student = (proposal.getStudent() != null) ? new ProposalStudentDTO(proposal.getStudent()) : null;
    }

    @Getter
    @Setter
    public static class ProposalRecruiterDTO {
        @Schema(description = "리크루터 ID", example = "1")
        private Long userId;

        @Schema(description = "회사 이름", example = "Tech Corp")
        private String company;

        @Schema(description = "리크루터 인증 정보", example = "Certified Recruiter")
        private String certification;

        @Schema(description = "사용자 이름", example = "리크루터 이름")
        private String username;

        public ProposalRecruiterDTO() {}

        public ProposalRecruiterDTO(Recruiter recruiter) {
            this.userId = (recruiter != null) ? recruiter.getUserId() : null;
            this.company = (recruiter != null) ? recruiter.getCompany() : null;
            this.certification = (recruiter != null) ? recruiter.getRecruiterCertificate() : null;
            this.username = (recruiter != null && recruiter.getUser() != null) ? recruiter.getUser().getUsername() : null;
        }
    }

    @Getter
    @Setter
    public static class ProposalStudentDTO {
        @Schema(description = "학생 ID", example = "2")
        private Long userId;

        @Schema(description = "전공", example = "Computer Science")
        private String major;

        @Schema(description = "포트폴리오 이미지 URL", example = "http://example.com/portfolio.jpg")
        private String portfolioImage;

        @Schema(description = "학교 이름", example = "Tech University")
        private String school;

        public ProposalStudentDTO() {}

        public ProposalStudentDTO(Student student) {
            this.userId = (student != null) ? student.getUserId() : null;
            this.major = (student != null) ? student.getMajor() : null;
            this.portfolioImage = (student != null) ? student.getPortfolioImage() : null;
            this.school = (student != null) ? student.getSchool() : null;
        }
    }
}