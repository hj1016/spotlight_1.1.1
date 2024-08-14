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
}
