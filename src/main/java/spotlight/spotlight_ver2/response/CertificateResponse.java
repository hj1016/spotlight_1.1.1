package spotlight.spotlight_ver2.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CertificateResponse {
    private boolean success;
    private String certificate;
    private String message;
}
