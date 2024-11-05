package katecam.luvicookie.ditto.domain.studymember.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record StudyMemberApplyRequest(
    @NotNull(message = "신청할 스터디 정보가 없습니다")
    @Min(value = 0, message = "잘못된 스터디 번호입니다")
    Integer studyId,
    String message
) {

}
