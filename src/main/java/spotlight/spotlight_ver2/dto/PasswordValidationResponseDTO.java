package spotlight.spotlight_ver2.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PasswordValidationResponseDTO {
    private boolean isValid;
    private String message;

    public static PasswordValidationResponseDTO success(String message) {
        return new PasswordValidationResponseDTO(true, message);
    }

    public static PasswordValidationResponseDTO failure(String message) {
        return new PasswordValidationResponseDTO(false, message);
    }
}