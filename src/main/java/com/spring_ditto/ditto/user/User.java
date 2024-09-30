package com.spring_ditto.ditto.user;

import jakarta.persistence.*;
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
    private Long id;
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
    //멘토인지 멘티인지
    @Column(name = "type")
    private String type;
    public void setType(String type){
        this.type = type;
    }
    public void authorizeUser(){
        this.role = Role.USER;
    }

    public void setNickname(String nickname){
        this.nickname = nickname;
    }
    public static User makeUser(UserDTO dto){
        User user = User.builder()
                .role(Role.USER)
                .type(dto.getType())
                .nickname(dto.getNickname())
                .build();
        return user;
    }
}
