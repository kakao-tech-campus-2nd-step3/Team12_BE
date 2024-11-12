package katecam.luvicookie.ditto.domain.member.dto.request;

import jakarta.validation.constraints.NotBlank;
import katecam.luvicookie.ditto.domain.member.domain.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class memberCreateRequest {
    @NotBlank(message = "이름을 입력해주세요.")
    String name;
    @NotBlank(message = "이메일을 입력해주세요.")
    String email;
    @NotBlank(message = "연락처를 입력해주세요.")
    String contact;
    @NotBlank(message = "닉네임을 입력해주세요.")
    String nickname;
    @NotBlank(message = "자기소개를 입력해주세요.")
    String description;

    public memberCreateRequest(Member member){
        this.name = member.getName();
        this.email = member.getEmail();
        this.contact = member.getContact();
        this.nickname = member.getNickname();
        this.description = member.getDescription();
    }
}
