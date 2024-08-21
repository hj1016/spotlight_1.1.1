package spotlight.spotlight_ver2.dto;

public class RecruiterDTO {
    private Long userId;
    private UserDTO user;
    private String company;
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