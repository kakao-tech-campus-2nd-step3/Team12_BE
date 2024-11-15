package katecam.luvicookie.ditto.domain.attendance.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import katecam.luvicookie.ditto.domain.attendance.domain.AttendanceDate;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

public record AttendanceListResponse(
        @JsonProperty("required_attendance")
        List<String> studyAttendanceDateList,

        @JsonProperty("member_attendance")
        Map<Integer, MemberAttendanceResponse> memberAttendances
) {
    public static AttendanceListResponse from(List<AttendanceDate> attendanceDates, Map<Integer, MemberAttendanceResponse> memberAttendances) {
        List<String> attendanceDateList = attendanceDates.stream()
                .map(date -> date.getStartTime()
                        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))
                )
                .toList();

        return new AttendanceListResponse(attendanceDateList, memberAttendances);
    }
}
