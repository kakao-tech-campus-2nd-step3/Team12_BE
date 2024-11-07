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
    FILE_CONVERT_FAILED("파일을 변환할 수 없습니다.", HttpStatus.BAD_REQUEST),
    AUTHENTICATION_FAILED("로그인을 다시 시도해주세요.", HttpStatus.FORBIDDEN),
    FILE_UPLOAD_FAILED("파일 업로드 도중 문제가 발생했습니다. 다시 시도해주세요.", HttpStatus.INTERNAL_SERVER_ERROR)
    ;

    private final String message;
    private final HttpStatus httpStatus;

}
