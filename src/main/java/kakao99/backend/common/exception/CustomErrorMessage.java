package kakao99.backend.common.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CustomErrorMessage {
    @NotNull
    private int statusCode;

    @NotNull
    private String message;

    private String place;

    public CustomErrorMessage(int statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;
    }

    public CustomErrorMessage(int statusCode, String message, String place) {
        this.statusCode = statusCode;
        this.message = message;
        this.place = place;
    }
}