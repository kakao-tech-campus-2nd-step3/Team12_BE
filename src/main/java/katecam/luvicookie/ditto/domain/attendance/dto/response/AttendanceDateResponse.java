package katecam.luvicookie.ditto.domain.attendance.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import katecam.luvicookie.ditto.domain.attendance.domain.AttendanceDate;
import lombok.Builder;

import java.time.format.DateTimeFormatter;

@Builder
public record AttendanceDateResponse(
        @JsonProperty("id")
        Integer dateId,

        @JsonProperty("start_time")
        String startTime,

        String deadline
) {

    public static AttendanceDateResponse from(AttendanceDate attendanceDate) {
        return AttendanceDateResponse.builder()
                .dateId(attendanceDate.getId())
                .startTime(attendanceDate.getStartTime()
                        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))
                )
                .deadline(attendanceDate.getDeadline()
                        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))
                )
                .build();
    }
}
