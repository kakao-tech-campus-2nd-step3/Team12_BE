package katecam.luvicookie.ditto.global.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    // 400 - Bad Request
    DATE_UNABLE_TO_ATTEND("현재 출석할 수 없습니다.", HttpStatus.BAD_REQUEST),
    INVALID_TOKEN("유효하지 않은 초대 토큰입니다", HttpStatus.BAD_REQUEST),
    INVALID_ROLE("잘못된 스터디 권한입니다", HttpStatus.BAD_REQUEST),
    ALREADY_STUDY_MEMBER("이미 가입된 스터디입니다", HttpStatus.BAD_REQUEST),
    INVALID_ATTENDANCE_DATE("잘못된 출석일자입니다.", HttpStatus.BAD_REQUEST),

    //401 - Unauthorized
    UNAUTHORIZED_TOKEN("유효하지 않은 액세스 토큰입니다", HttpStatus.UNAUTHORIZED),

    // 403 - Forbidden
    NOT_STUDY_LEADER("스터디장이 아닙니다.", HttpStatus.FORBIDDEN),
    NOT_STUDY_MEMBER("스터디원이 아닙니다.", HttpStatus.FORBIDDEN),
    WRITER_NOT_MATCH("작성자가 일치하지 않습니다.", HttpStatus.FORBIDDEN),
    AUTHENTICATION_FAILED("로그인을 다시 시도해주세요.", HttpStatus.FORBIDDEN),

    // 404 - Not Found
    STUDY_NOT_FOUND("존재하지 않는 스터디입니다.", HttpStatus.NOT_FOUND),
    MEMBER_NOT_FOUND("존재하지 않는 회원입니다.", HttpStatus.NOT_FOUND),
    NOTICE_NOT_FOUND("존재하지 않는 공지입니다.", HttpStatus.NOT_FOUND),
    ASSIGNMENT_NOT_FOUND("존재하지 않는 과제입니다.", HttpStatus.NOT_FOUND),
    FILE_NOT_FOUND("존재하지 않는 파일입니다.", HttpStatus.NOT_FOUND),
    DATE_NOT_FOUND("존재하지 않는 출석일자입니다.", HttpStatus.NOT_FOUND),
    STUDY_MEMBER_NOT_FOUND("존재하지 않는 스터디원입니다.", HttpStatus.NOT_FOUND),
    STUDY_LEADER_NOT_FOUND("스터디장이 존재하지 않습니다.", HttpStatus.NOT_FOUND),

    // 409 - Conflict
    CODE_MISMATCH("코드가 일치하지 않습니다.", HttpStatus.CONFLICT),
    ALREADY_ATTENDED("해당 일자는 이미 출석하였습니다.", HttpStatus.CONFLICT),
    NOT_ATTENDED("해당 일자는 출석하지 않았습니다.", HttpStatus.CONFLICT),

    // 500 - Internal Server Error
    FILE_CONVERT_FAILED("파일을 변환할 수 없습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    FILE_UPLOAD_FAILED("파일 업로드 도중 문제가 발생했습니다. 다시 시도해주세요.", HttpStatus.INTERNAL_SERVER_ERROR)
    ;

    private final String message;
    private final HttpStatus httpStatus;

}
