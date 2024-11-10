package spotlight.spotlight_ver2.request;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@Component
public class CertificateRequest {
    private MultipartFile certificate;
    private String username;
}
