package katecam.luvicookie.ditto.domain.attendance.dto.response;

import katecam.luvicookie.ditto.domain.attendance.domain.Attendance;
import katecam.luvicookie.ditto.domain.attendance.domain.AttendanceDate;

import java.time.format.DateTimeFormatter;
import java.util.List;

public record MemberAttendanceResponse(
        List<String> memberAttendanceDateList,
        Double attendanceRate
) {
    public static MemberAttendanceResponse from(List<AttendanceDate> studyAttendanceDateList, List<Attendance> memberAttendanceList) {
        List<String> memberAttendanceDateList = memberAttendanceList.stream()
                .map(attendance -> attendance.getAttendanceDate()
                        .getStartTime()
                        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))
                )
                .toList();
        Double attendanceRate = (double) memberAttendanceDateList.size() / studyAttendanceDateList.size();

        return new MemberAttendanceResponse(memberAttendanceDateList, attendanceRate);
    }

}
