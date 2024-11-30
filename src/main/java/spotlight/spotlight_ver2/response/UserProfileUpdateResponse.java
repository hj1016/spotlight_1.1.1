package spotlight.spotlight_ver2.response;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class UserProfileUpdateResponse {
    private boolean success;
    private String name;
    private String profileImageUrl;
}
