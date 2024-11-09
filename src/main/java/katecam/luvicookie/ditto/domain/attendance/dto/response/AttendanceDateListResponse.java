package katecam.luvicookie.ditto.domain.attendance.dto.response;

import katecam.luvicookie.ditto.domain.attendance.domain.AttendanceDate;

import java.time.format.DateTimeFormatter;
import java.util.List;

public record AttendanceDateListResponse (
        List<String> attendanceDateList
) {
    public static AttendanceDateListResponse from(List<AttendanceDate> attendanceDates) {
        List<String> attendanceDateList = attendanceDates.stream()
                .map(attendanceDate -> attendanceDate.getStartTime()
                        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))
                )
                .toList();

        return new AttendanceDateListResponse(attendanceDateList);
    }

}
