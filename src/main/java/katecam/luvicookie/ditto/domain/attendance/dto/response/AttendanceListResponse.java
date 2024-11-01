package katecam.luvicookie.ditto.domain.attendance.dto.response;

import katecam.luvicookie.ditto.domain.attendance.domain.AttendanceDates;

import java.util.List;
import java.util.Map;

public record AttendanceListResponse(
        AttendanceDateListResponse attendanceDateList,
        Map<Integer, MemberAttendanceResponse> memberAttendances
) {
    public static AttendanceListResponse from(List<AttendanceDates> attendanceDatesList, Map<Integer, MemberAttendanceResponse> memberAttendances) {
        AttendanceDateListResponse attendanceDateList = AttendanceDateListResponse.from(attendanceDatesList);
        return new AttendanceListResponse(attendanceDateList, memberAttendances);
    }
}
