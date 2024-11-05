package katecam.luvicookie.ditto.domain.studymember.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import katecam.luvicookie.ditto.domain.member.domain.Member;

public record MemberResponse (
    @JsonProperty("id")
    Integer id,
    @JsonProperty("nickname")
    String nickname,
    @JsonProperty("description")
    String description,
    @JsonProperty("profile_image")
    String profileImage
) {

    public MemberResponse(Member member) {
        this(member.getId(), member.getNickname(), member.getDescription(), member.getProfileImage());
    }
}
