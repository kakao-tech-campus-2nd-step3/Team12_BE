package katecam.luvicookie.ditto.global.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    STUDY_NOT_FOUND("존재하지 않는 스터디입니다.", HttpStatus.NOT_FOUND),
    MEMBER_NOT_FOUND("존재하지 않는 회원입니다.", HttpStatus.NOT_FOUND),
    NOTICE_NOT_FOUND("존재하지 않는 공지입니다." ,HttpStatus.NOT_FOUND),
    DATE_UNABLE_TO_ATTEND("현재 출석할 수 없습니다.", HttpStatus.BAD_REQUEST),
    NOT_STUDY_LEADER("스터디장이 아닙니다.", HttpStatus.FORBIDDEN),
    NOT_STUDY_MEMBER("스터디원이 아닙니다.", HttpStatus.FORBIDDEN),
    INVALID_ROLE("잘못된 스터디 권한입니다", HttpStatus.BAD_REQUEST)
    ;

    private final String message;
    private final HttpStatus httpStatus;

}
