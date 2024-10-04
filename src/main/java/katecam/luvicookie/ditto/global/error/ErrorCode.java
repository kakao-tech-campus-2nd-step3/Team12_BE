package katecam.luvicookie.ditto.global.error;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    STUDY_NOT_FOUND("존재하지 않는 스터디입니다.", HttpStatus.NOT_FOUND)
    ;

    private final String message;
    private final HttpStatus httpStatus;

    ErrorCode(String message, HttpStatus httpStatus) {
        this.message = message;
        this.httpStatus = httpStatus;
    }

}
