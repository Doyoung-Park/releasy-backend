package kakao99.backend.utils;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
public class ResponseMessage {

    private Integer statusCode;
    private String message;
    private Object data;

    public ResponseMessage createMessage(Integer statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;
        return this;
    }

    public ResponseMessage createMessage(Integer statusCode, String message, Object data) {
        this.statusCode = statusCode;
        this.message = message;
        this.data = data;
        return this;
    }
}
