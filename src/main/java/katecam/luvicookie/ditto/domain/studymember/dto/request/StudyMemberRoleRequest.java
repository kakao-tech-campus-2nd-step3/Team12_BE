package katecam.luvicookie.ditto.domain.studymember.dto.request;

import jakarta.validation.constraints.NotBlank;
import katecam.luvicookie.ditto.domain.studymember.domain.StudyMemberRole;
import katecam.luvicookie.ditto.global.error.ErrorCode;
import katecam.luvicookie.ditto.global.error.GlobalException;

public record StudyMemberRoleRequest(
    @NotBlank(message = "스터디 역할을 입력해주세요.")
    String role
) {
    public StudyMemberRole toStudyMemberRole() {
        return switch (role) {
            case "스터디장" -> StudyMemberRole.LEADER;
            case "스터디원" -> StudyMemberRole.MEMBER;
            case "신청" -> StudyMemberRole.APPLICANT;
            case "탈퇴" -> StudyMemberRole.WITHDRAWN;
            default -> throw new GlobalException(ErrorCode.INVALID_ROLE);
        };
    }
}
