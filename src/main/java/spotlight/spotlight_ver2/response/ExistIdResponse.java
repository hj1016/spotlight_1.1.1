package spotlight.spotlight_ver2.response;

public class ExistIdResponse {
    private boolean success;

    public ExistIdResponse(boolean success) {
        this.success = success;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
