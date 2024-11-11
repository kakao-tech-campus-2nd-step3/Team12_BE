package katecam.luvicookie.ditto.domain.attendance.dto.response;

public record AttendanceCodeResponse(
        String code
) {
    public static AttendanceCodeResponse from(String code) {
        return new AttendanceCodeResponse(code);
    }
}
