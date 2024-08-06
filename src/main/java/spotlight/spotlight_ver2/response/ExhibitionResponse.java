package spotlight.spotlight_ver2.response;

import spotlight.spotlight_ver2.DTO.ExhibitionDTO;

public class ExhibitionResponse {
    private boolean success;
    private ExhibitionDTO exhibition;

    public ExhibitionResponse(boolean success, ExhibitionDTO exhibition) {
        this.success = success;
        this.exhibition = exhibition;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public ExhibitionDTO getExhibition() {
        return exhibition;
    }

    public void setExhibition(ExhibitionDTO exhibition) {
        this.exhibition = exhibition;
    }
}