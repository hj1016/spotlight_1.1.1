package spotlight.spotlight_ver2.response;

import spotlight.spotlight_ver2.entity.User;

public class UserResponse {
    private User user;
    private boolean success;
    private String message;

    public UserResponse() {
    }

    public UserResponse(User user, boolean success, String message) {
        this.user = user;
        this.success = success;
        this.message = message;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
