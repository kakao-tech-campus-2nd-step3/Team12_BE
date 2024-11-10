package katecam.luvicookie.ditto.domain.member.domain;

import jakarta.persistence.*;
import katecam.luvicookie.ditto.domain.member.dto.memberDTO;
import lombok.*;

@Table(name = "member")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
@Entity
@AllArgsConstructor
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Column(name = "name")
    private String name;
    @Column(name = "email")
    private String email;
    @Column(name = "contact")
    private String contact;
    @Column(name = "nickname")
    private String nickname;
    @Column(name = "description")
    private String description;
    @Column(name = "profile_image")
    private String profileImage;
    //최초 로그인인지 아닌지 알아보기 위한 용도
    @Enumerated(EnumType.STRING)
    private Role role;

    @Builder
    public Member(String name, String email, String contact, String nickname, String description, String profileImage, Role role){
        this.name = name;
        this.email = email;
        this.contact = contact;
        this.nickname = nickname;
        this.description = description;
        this.profileImage = profileImage;
        this.role = role;
    }


    public void authorizeUser(){
        this.role = Role.USER;
    }

    public boolean isGuest() {
        return Role.GUEST.equals(role);
    }
}
