package katecam.luvicookie.ditto.domain.member.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class profileImageDTO {
    String profileImage;

    public profileImageDTO(String profileImage) {
        this.profileImage = profileImage;
    }
}
