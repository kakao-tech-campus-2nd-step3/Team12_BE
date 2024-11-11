package katecam.luvicookie.ditto.domain.attendance.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import static katecam.luvicookie.ditto.domain.attendance.domain.AttendanceDate.ATTENDANCE_CODE_LENGTH;

public record AttendanceCodeRequest(
        @NotBlank(message = "출석코드를 입력해주세요.")
        @Size(min = ATTENDANCE_CODE_LENGTH, max = ATTENDANCE_CODE_LENGTH, message = "잘못된 코드 형식입니다.")
        String code,

        @NotNull(message = "출석일자 ID를 입력해주세요.")
        Integer dateId
) {
}
