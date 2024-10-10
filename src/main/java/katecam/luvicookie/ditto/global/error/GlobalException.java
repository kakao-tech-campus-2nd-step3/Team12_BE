package katecam.luvicookie.ditto.global.error;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class GlobalException extends RuntimeException {

    private final String message;
    private final HttpStatus httpStatus;

    public GlobalException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        message = errorCode.getMessage();
        httpStatus = errorCode.getHttpStatus();
    }

}
