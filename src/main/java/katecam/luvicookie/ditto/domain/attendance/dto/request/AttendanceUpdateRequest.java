package katecam.luvicookie.ditto.domain.attendance.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;

public record AttendanceUpdateRequest(
        @NotNull(message = "출석일자 ID를 입력해주세요.")
        @JsonProperty("date_id")
        Integer dateId,

        @NotNull(message = "출석 여부를 입력해주세요.")
        @JsonProperty("is_attended")
        Boolean isAttended
) {
}
