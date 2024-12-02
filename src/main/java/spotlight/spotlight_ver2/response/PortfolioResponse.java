package spotlight.spotlight_ver2.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PortfolioResponse {
    private boolean success;
    private String message;
    private List<String> portfolioList;

    public PortfolioResponse(boolean success, String message, List<String> portfolioList) {
        this.success = success;
        this.message = message;
        this.portfolioList = portfolioList;
    }
}
