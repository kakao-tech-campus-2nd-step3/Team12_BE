package katecam.luvicookie.ditto.domain.attendance.dto.response;

import katecam.luvicookie.ditto.domain.attendance.domain.AttendanceDate;

import java.util.List;
import java.util.Map;

public record AttendanceListResponse(
        AttendanceDateListResponse attendanceDateList,
        Map<Integer, MemberAttendanceResponse> memberAttendances
) {
    public static AttendanceListResponse from(List<AttendanceDate> attendanceDatesList, Map<Integer, MemberAttendanceResponse> memberAttendances) {
        AttendanceDateListResponse attendanceDateList = AttendanceDateListResponse.from(attendanceDatesList);
        return new AttendanceListResponse(attendanceDateList, memberAttendances);
    }
}
