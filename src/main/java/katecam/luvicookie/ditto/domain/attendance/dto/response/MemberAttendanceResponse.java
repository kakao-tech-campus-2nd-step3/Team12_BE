package katecam.luvicookie.ditto.domain.attendance.dto.response;

import katecam.luvicookie.ditto.domain.attendance.domain.AttendanceDate;

import java.time.format.DateTimeFormatter;
import java.util.List;

public record MemberAttendanceResponse(
        List<String> memberAttendanceDateStringList,
        Double attendanceRate
) {
    public static MemberAttendanceResponse from(List<AttendanceDate> studyAttendanceDateList, List<AttendanceDate> memberAttendanceDateList) {
        List<String> memberAttendanceDateStringList = memberAttendanceDateList.stream()
                .map(date -> date.getStartTime()
                        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))
                )
                .toList();
        Double attendanceRate = (double) memberAttendanceDateList.size() / studyAttendanceDateList.size();

        return new MemberAttendanceResponse(memberAttendanceDateStringList, attendanceRate);
    }

}
