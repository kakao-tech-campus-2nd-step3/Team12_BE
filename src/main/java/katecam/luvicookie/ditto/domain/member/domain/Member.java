package katecam.luvicookie.ditto.domain.member.domain;

import jakarta.persistence.*;
import katecam.luvicookie.ditto.domain.member.dto.memberDTO;
import lombok.*;

@Table(name = "member")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
@Entity
@Builder
@AllArgsConstructor
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Column(name = "name")
    private String name;
    @Column(name = "nickname")
    private String nickname;
    //최초 로그인인지 아닌지 알아보기 위한 용도
    @Enumerated(EnumType.STRING)
    private Role role;
    @Column(name = "email")
    private String email;
    @Column(name = "contact")
    private String contact;
    public void authorizeUser(){
        this.role = Role.USER;
    }
    public static Member toEntity(memberDTO dto){
        return Member.builder()
                .role(Role.USER)
                .email(dto.getEmail())
                .build();
    }

    public boolean isGuest() {
        return role.equals(Role.GUEST);
    }
}
