package kakao99.backend.issue.exception;

import lombok.Getter;

@Getter
public class CustomException extends RuntimeException{
    private int statusCode;
    private String place;
    public CustomException() {
        super();
    }

    public CustomException(String message) {
        super(message);
    }
    public CustomException(int statusCode, String message) {
        super(message);
        this.statusCode = statusCode;
    }

    public CustomException(int statusCode, String message, String place) {
        super(message);
        this.statusCode = statusCode;
        this.place = place;
    }
}
