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
    ASSIGNMENT_NOT_FOUND("존재하지 않는 과제입니다.", HttpStatus.NOT_FOUND),
    WRITER_NOT_MATCH("작성자가 일치하지 않습니다.", HttpStatus.FORBIDDEN),
    FILE_NOT_FOUND("존재하지 않는 파일입니다.", HttpStatus.NOT_FOUND)
    ;

    private final String message;
    private final HttpStatus httpStatus;

}
