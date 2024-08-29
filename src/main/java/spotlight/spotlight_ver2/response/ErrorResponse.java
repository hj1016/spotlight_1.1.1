package spotlight.spotlight_ver2.response;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ErrorResponse {
    private String message;

    public static ResponseEntity<ErrorResponse> toResponseEntity(HttpStatus status, String message) {
        return new ResponseEntity<>(new ErrorResponse(message), status);
    }

    public ErrorResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
