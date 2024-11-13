package katecam.luvicookie.ditto.fixture;

import katecam.luvicookie.ditto.domain.member.domain.Member;
import katecam.luvicookie.ditto.domain.studymember.domain.StudyMember;

import java.time.LocalDateTime;

public class StudyMemberFixture extends StudyMember {

    private final LocalDateTime testJoinedAt;

    public StudyMemberFixture(LocalDateTime testJoinedAt) {
        super();
        this.testJoinedAt = testJoinedAt;
    }

    @Override
    public LocalDateTime getJoinedAt() {
        return testJoinedAt;
    }

    @Override
    public Member getMember() {
        return Member.builder()
                .build();
    }

}
