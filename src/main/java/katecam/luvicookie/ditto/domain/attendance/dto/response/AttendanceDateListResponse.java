package katecam.luvicookie.ditto.domain.attendance.dto.response;

import katecam.luvicookie.ditto.domain.attendance.domain.AttendanceDates;

import java.time.format.DateTimeFormatter;
import java.util.List;

public record AttendanceDateListResponse (
        List<String> attendanceDateList
) {
    public static AttendanceDateListResponse from(List<AttendanceDates> attendanceDatesList) {
        List<String> attendanceDateList = attendanceDatesList.stream()
                .map(attendanceDates -> attendanceDates.getAttendanceDate()
                        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))
                )
                .toList();

        return new AttendanceDateListResponse(attendanceDateList);
    }

}
