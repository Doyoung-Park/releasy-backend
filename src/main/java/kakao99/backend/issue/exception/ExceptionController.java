package kakao99.backend.issue.exception;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class ExceptionController extends ResponseEntityExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<?> testException(CustomException e) {
        System.out.println("Exception!!!");

        CustomErrorMessage customErrorMessage = new CustomErrorMessage(999, e.getMessage(), e.getPlace());

        return new ResponseEntity<>(customErrorMessage, HttpStatus.NOT_IMPLEMENTED);
    }

}
