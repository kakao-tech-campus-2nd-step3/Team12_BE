package katecam.luvicookie.ditto.domain.attendance.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record AttendanceUpdateRequest(
        @NotNull(message = "출석일자를 입력해주세요.")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm", timezone = "Asia/Seoul")
        @JsonProperty("datetime")
        LocalDateTime dateTime,

        @NotNull(message = "출석 여부를 입력해주세요.")
        @JsonProperty("is_attended")
        Boolean isAttended
) {
}
