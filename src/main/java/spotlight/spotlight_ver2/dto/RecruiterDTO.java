package spotlight.spotlight_ver2.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import spotlight.spotlight_ver2.entity.Recruiter;
import spotlight.spotlight_ver2.entity.User;

@Getter
@Setter
@Schema(description = "리크루터 DTO")
public class RecruiterDTO {

    @Schema(description = "사용자 ID", example = "1")
    private Long userId;

    @Schema(description = "회사", example = "네이버")
    private String company;

    @Schema(description = "재직 증명서 URL", example = "http://example.com/certification.jpg")
    private String recruiterCertificate;

    @Schema(description = "사용자 정보", implementation = RecruiterUserDTO.class)
    private RecruiterUserDTO user;

    public RecruiterDTO(Recruiter recruiter) {
        this.userId = recruiter.getUserId();
        this.company = recruiter.getCompany();
        this.recruiterCertificate = recruiter.getRecruiterCertificate();

        // User 객체가 null이 아닐 때만 RecruiterUserDTO 초기화
        this.user = (recruiter.getUser() != null) ? new RecruiterUserDTO(recruiter.getUser()) : null;
    }

    @Getter
    public static class RecruiterUserDTO {
        @Schema(description = "사용자 ID", example = "1")
        private Long id;

        @Schema(description = "사용자 이름", example = "김학생")
        private String username;

        @Schema(description = "이메일 주소", example = "김학생@example.com")
        private String email;

        @Schema(description = "사용자 실명", example = "김학생")
        private String name;

        public RecruiterUserDTO(User user){
            this.id = user.getId();
            this.username = user.getUsername();
            this.email = user.getEmail();
            this.name = user.getName();
        }
    }
}