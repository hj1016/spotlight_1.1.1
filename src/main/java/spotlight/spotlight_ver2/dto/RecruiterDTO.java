package spotlight.spotlight_ver2.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "리크루터 DTO")
public class RecruiterDTO {

    @Schema(description = "사용자 ID", example = "1")
    private Long userId;

    @Schema(description = "사용자 정보", implementation = UserDTO.class)
    private UserDTO user;

    @Schema(description = "회사", example = "네이버")
    private String company;

    @Schema(description = "재직 증명서 URL", example = "http://example.com/certification.jpg")
    private String certification;

    // 기본 생성자
    public RecruiterDTO() {}

    // 매개변수를 받는 생성자
    public RecruiterDTO(Long userId, UserDTO user, String company, String certification) {
        this.userId = userId;
        this.user = user;
        this.company = company;
        this.certification = certification;
    }

    // Getters and Setters
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getCertification() {
        return certification;
    }

    public void setCertification(String certification) {
        this.certification = certification;
    }
}