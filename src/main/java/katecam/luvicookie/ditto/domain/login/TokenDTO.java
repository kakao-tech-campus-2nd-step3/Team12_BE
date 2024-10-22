package katecam.luvicookie.ditto.domain.login;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TokenDTO {
    String token;

    public TokenDTO(String token) {
        this.token = token;
    }
}
