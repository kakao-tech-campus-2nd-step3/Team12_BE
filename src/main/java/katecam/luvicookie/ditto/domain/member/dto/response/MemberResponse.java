package katecam.luvicookie.ditto.domain.member.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import katecam.luvicookie.ditto.domain.member.domain.Member;
import lombok.Builder;

@Builder
public record MemberResponse(
        Integer id,
        String name,
        String email,
        String contact,
        String nickname,
        String description,
        @JsonProperty("profile_image")
        String profileImage
){
    public static MemberResponse from(Member member) {
        return MemberResponse.builder()
                .id(member.getId())
                .name(member.getName())
                .email(member.getEmail())
                .contact(member.getContact())
                .nickname(member.getNickname())
                .description(member.getDescription())
                .profileImage(member.getProfileImage())
                .build();
    }
}
