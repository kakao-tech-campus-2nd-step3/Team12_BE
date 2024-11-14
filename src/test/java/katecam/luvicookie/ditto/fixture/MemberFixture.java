package katecam.luvicookie.ditto.fixture;

import katecam.luvicookie.ditto.domain.member.domain.Member;

public class MemberFixture extends Member {

    private static final String DEFAULT_EMAIL = "test_member@email.com";
    private final Integer memberId;

    public MemberFixture(Integer memberId) {
        super();
        this.memberId = memberId;
    }

    @Override
    public Integer getId() {
        return memberId;
    }

    @Override
    public String getEmail() {
        return DEFAULT_EMAIL;
    }

}
