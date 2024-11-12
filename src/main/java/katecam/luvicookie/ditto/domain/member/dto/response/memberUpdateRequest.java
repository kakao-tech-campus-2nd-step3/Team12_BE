package katecam.luvicookie.ditto.domain.member.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public record memberUpdateRequest (
        String name,
        String contact,
        String nickname,
        String description
){ }
