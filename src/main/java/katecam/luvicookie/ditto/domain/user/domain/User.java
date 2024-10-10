package katecam.luvicookie.ditto.domain.user.domain;

import jakarta.persistence.*;
import katecam.luvicookie.ditto.domain.user.dto.UserDTO;
import lombok.*;

@Table(name = "user_tb")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Builder
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Integer id;
    @Column(name = "name")
    private String name;

    @Column(name = "nickname")
    private String nickname;
    //socialId로 유저 구분 가능
    @Column(name = "socialId")
    private String socialId;
    //최초 로그인인지 아닌지 알아보기 위한 용도
    @Enumerated(EnumType.STRING)
    private Role role;

    public void authorizeUser(){
        this.role = Role.USER;
    }

    public void setNickname(String nickname){
        this.nickname = nickname;
    }
    public static User toEntity(UserDTO dto){
        return User.builder()
                .role(Role.USER)
                .nickname(dto.getNickname())
                .build();
    }

    public boolean isGuest() {
        return role.equals(Role.GUEST);
    }
}
