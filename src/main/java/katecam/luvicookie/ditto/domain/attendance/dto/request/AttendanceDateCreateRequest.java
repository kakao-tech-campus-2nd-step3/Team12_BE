package katecam.luvicookie.ditto.domain.attendance.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record AttendanceDateCreateRequest(
        @NotNull(message = "출석일자를 입력해주세요.")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm", timezone = "Asia/Seoul")
        LocalDateTime startTime,

        @NotNull(message = "출석 허용 시간을 입력해주세요.")
        Integer intervalMinutes
) {
}
