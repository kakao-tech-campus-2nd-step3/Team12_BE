package katecam.luvicookie.ditto.domain.attendance.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record AttendanceDateDeleteRequest(
        @NotNull(message = "출석 시작 시간을 입력해주세요.")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm", timezone = "Asia/Seoul")
        @JsonProperty("start_time")
        LocalDateTime startTime
) {
}
