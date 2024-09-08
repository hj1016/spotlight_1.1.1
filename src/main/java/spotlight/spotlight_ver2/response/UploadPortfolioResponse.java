package spotlight.spotlight_ver2.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UploadPortfolioResponse {
    private boolean success;
    private String message;
    private List<String> portfolioList;

}
