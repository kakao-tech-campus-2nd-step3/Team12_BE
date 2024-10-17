package katecam.luvicookie.ditto.domain.member.dto;

import katecam.luvicookie.ditto.domain.member.domain.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class memberRequestDTO {
    String name;
    String email;
    String contact;
    String nickname;
    String description;

    public memberRequestDTO(Member member){
        this.name = member.getName();
        this.email = member.getEmail();
        this.contact = member.getContact();
        this.nickname = member.getNickname();
        this.description = member.getDescription();
    }
}
