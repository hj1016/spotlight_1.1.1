package spotlight.spotlight_ver2.request;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class UserProfileUpdateRequest {
    private String name;
    private MultipartFile profileImage;
}
