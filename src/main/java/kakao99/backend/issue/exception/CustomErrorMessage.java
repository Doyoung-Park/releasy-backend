package kakao99.backend.issue.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CustomErrorMessage {
    @NotNull
    private Integer statusCode;

    @NotNull
    private String message;

    private String place;

    public CustomErrorMessage(Integer statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;
    }

    public CustomErrorMessage(Integer statusCode, String message, String place) {
        this.statusCode = statusCode;
        this.message = message;
        this.place = place;
    }
}
