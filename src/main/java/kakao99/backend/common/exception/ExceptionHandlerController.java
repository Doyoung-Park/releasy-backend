package kakao99.backend.common.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.NoSuchElementException;

@RestControllerAdvice
@Slf4j
public class ExceptionHandlerController extends ResponseEntityExceptionHandler{

        @ExceptionHandler(CustomException.class)
        public ResponseEntity<?> customExceptionHandler(CustomException e) {

            CustomErrorMessage customErrorMessage = (e.getPlace() != null) ?
                    new CustomErrorMessage(e.getStatusCode(), e.getMessage(), e.getPlace())
                    : new CustomErrorMessage(e.getStatusCode(), e.getMessage());
            return new ResponseEntity<>(customErrorMessage, HttpStatus.OK);
        }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<?> noSuchElementExceptionHandler(NoSuchElementException e) {

        CustomErrorMessage customErrorMessage = new CustomErrorMessage(404, e.getMessage());
        return new ResponseEntity<>(customErrorMessage, HttpStatus.OK);
    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<?> nullPointExceptionHandler(NullPointerException e) {

        CustomErrorMessage customErrorMessage = new CustomErrorMessage(500, e.getMessage());
        return new ResponseEntity<>(customErrorMessage, HttpStatus.OK);
    }


}
