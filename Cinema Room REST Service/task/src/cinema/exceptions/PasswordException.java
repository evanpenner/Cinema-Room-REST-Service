package cinema.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNAUTHORIZED)
public class PasswordException extends RuntimeException {
    public PasswordException(String message) {
        super(message);
    }
}
