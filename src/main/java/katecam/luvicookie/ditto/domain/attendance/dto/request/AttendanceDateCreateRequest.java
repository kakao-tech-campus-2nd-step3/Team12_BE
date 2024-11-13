package katecam.luvicookie.ditto.domain.attendance.dto.request;

import  com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record AttendanceDateCreateRequest(
        @NotNull(message = "출석일자를 입력해주세요.")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm", timezone = "Asia/Seoul")
        @JsonProperty("start_time")
        LocalDateTime startTime,

        @NotNull(message = "출석 허용 시간을 입력해주세요.")
        @Min(value = 1, message = "출석 허용 시간을 양의 정수 값으로 입력해주세요.")
        @JsonProperty("time_interval")
        Integer intervalMinutes
) {
}
