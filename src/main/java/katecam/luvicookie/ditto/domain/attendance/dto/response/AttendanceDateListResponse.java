package katecam.luvicookie.ditto.domain.attendance.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import katecam.luvicookie.ditto.domain.attendance.domain.AttendanceDate;

import java.util.List;

public record AttendanceDateListResponse (
        @JsonProperty("attendance_date_list")
        List<AttendanceDateResponse> attendanceDateList
) {
    public static AttendanceDateListResponse from(List<AttendanceDate> attendanceDates) {
        List<AttendanceDateResponse> attendanceDateList = attendanceDates.stream()
                .map(AttendanceDateResponse::from)
                .toList();

        return new AttendanceDateListResponse(attendanceDateList);
    }

}
