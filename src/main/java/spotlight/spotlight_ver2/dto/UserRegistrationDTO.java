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
}
