package katecam.luvicookie.ditto.domain.attendance.dto.response;

import katecam.luvicookie.ditto.domain.attendance.domain.Attendance;
import katecam.luvicookie.ditto.domain.attendance.domain.AttendanceDates;

import java.time.format.DateTimeFormatter;
import java.util.List;

public record MemberAttendanceResponse(
        List<String> memberAttendanceDateList,
        Double attendanceRate
) {
    public static MemberAttendanceResponse from(List<AttendanceDates> studyAttendanceDatesList, List<Attendance> memberAttendanceList) {
        List<String> memberAttendanceDateList = memberAttendanceList.stream()
                .map(attendance -> attendance.getAttendanceDates()
                        .getAttendanceDate()
                        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))
                )
                .toList();
        Double attendanceRate = (double) memberAttendanceDateList.size() / studyAttendanceDatesList.size();

        return new MemberAttendanceResponse(memberAttendanceDateList, attendanceRate);
    }

}
