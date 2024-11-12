package katecam.luvicookie.ditto.domain.attendance.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import katecam.luvicookie.ditto.domain.attendance.domain.AttendanceDate;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public record MemberAttendanceResponse(
        @JsonProperty("member_attendance_list")
        List<String> memberAttendanceDateStringList,

        @JsonProperty("attendance_rate")
        Double attendanceRate
) {
    public static MemberAttendanceResponse from(List<AttendanceDate> studyAttendanceDateList, List<LocalDateTime> memberAttendanceDateList) {
        List<String> memberAttendanceDateStringList = memberAttendanceDateList.stream()
                .map(dateTime -> dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")))
                .toList();

        if (studyAttendanceDateList.isEmpty()) {
            return new MemberAttendanceResponse(memberAttendanceDateStringList, 0.0);
        }

        Double attendanceRate = (double) memberAttendanceDateList.size() / studyAttendanceDateList.size();

        return new MemberAttendanceResponse(memberAttendanceDateStringList, attendanceRate);
    }

}
