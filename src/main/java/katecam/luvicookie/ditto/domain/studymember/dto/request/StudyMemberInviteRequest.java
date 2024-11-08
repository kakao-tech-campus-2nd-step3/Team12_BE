package katecam.luvicookie.ditto.domain.studymember.dto.request;

import jakarta.validation.constraints.NotBlank;

public record StudyMemberInviteRequest(
        @NotBlank(message = "스터디 초대 토큰이 존재하지 않습니다.")
        String token
) {

}
