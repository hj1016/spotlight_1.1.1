package spotlight.spotlight_ver2.dto;

public class PasswordValidationDTO {
    private String password;

    // 기본 생성자
    public PasswordValidationDTO() {
    }

    // 파라미터가 있는 생성자
    public PasswordValidationDTO(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
