package katecam.luvicookie.ditto.domain.studymember.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum StudyMemberRole {
    LEADER("스터디장"), MEMBER("스터디원"), APPLICANT("신청"), REJECTED("거절"), WITHDRAWN("탈퇴");
    private final String value;
}
