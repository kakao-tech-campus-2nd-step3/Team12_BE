package katecam.luvicookie.ditto.domain.studymember.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import katecam.luvicookie.ditto.domain.member.domain.Member;

public record StudyLeaderResponse(
        String nickname,

        @JsonProperty("profile_image")
        String profileImage,

        @JsonProperty("number_of_people")
        Integer numberOfPeople
) {
    public StudyLeaderResponse(Member member, Integer numberOfPeople) {
        this(member.getNickname(), member.getProfileImage(), numberOfPeople);
    }
}
