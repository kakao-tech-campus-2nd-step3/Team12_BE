package katecam.luvicookie.ditto.domain.studymember.dto.request;

import jakarta.validation.constraints.NotBlank;
import katecam.luvicookie.ditto.domain.studymember.domain.StudyMemberRole;
import katecam.luvicookie.ditto.global.error.ErrorCode;
import katecam.luvicookie.ditto.global.error.GlobalException;

public record StudyMemberRoleRequest(
        @NotBlank(message = "스터디 역할을 입력해주세요.")
        String role
) {

}
