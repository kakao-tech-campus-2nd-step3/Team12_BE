package katecam.luvicookie.ditto.global.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    STUDY_NOT_FOUND("존재하지 않는 스터디입니다.", HttpStatus.NOT_FOUND)
    ;

    private final String message;
    private final HttpStatus httpStatus;

}