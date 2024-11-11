package katecam.luvicookie.ditto.domain.studymember.domain;

import katecam.luvicookie.ditto.global.error.ErrorCode;
import katecam.luvicookie.ditto.global.error.GlobalException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum StudyMemberRole {
    LEADER("스터디장"), MEMBER("스터디원"), APPLICANT("신청"), REJECTED("거절"), WITHDRAWN("탈퇴");
    private final String value;

    public static StudyMemberRole from(String role) {
        for (StudyMemberRole studyMemberRole : StudyMemberRole.values()) {
            if (studyMemberRole.value.equals(role)) {
                return studyMemberRole;
            }
        }
        throw new GlobalException(ErrorCode.INVALID_ROLE);
    }
}
