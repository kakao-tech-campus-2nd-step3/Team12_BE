package katecam.luvicookie.ditto.domain.member.dto.response;

import katecam.luvicookie.ditto.domain.member.domain.Member;
import katecam.luvicookie.ditto.domain.notice.domain.Notice;
import katecam.luvicookie.ditto.domain.notice.dto.response.NoticeResponse;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
public record memberResponse (
        Integer id,
        String name,
        String email,
        String contact,
        String nickname,
        String description,
        String profileImage
){
    public static memberResponse from(Member member) {
        this.id = member.getId();
        this.name = member.getName();
        this.email = member.getEmail();
        this.contact = member.getContact();
        this.nickname = member.getNickname();
        this.description = member.getDescription();
        this.profileImage = member.getProfileImage();
    }
}
