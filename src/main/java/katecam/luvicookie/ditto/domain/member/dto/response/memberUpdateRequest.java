package katecam.luvicookie.ditto.domain.member.dto.response;

import katecam.luvicookie.ditto.domain.member.domain.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class memberUpdateRequest {
    String name;
    String contact;
    String nickname;
    String description;

    public memberUpdateRequest(Member member){
        this.name = member.getName();
        this.contact = member.getContact();
        this.nickname = member.getNickname();
        this.description = member.getDescription();
    }
}
