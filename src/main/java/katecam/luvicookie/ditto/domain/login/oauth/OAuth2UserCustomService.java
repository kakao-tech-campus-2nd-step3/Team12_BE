package katecam.luvicookie.ditto.domain.login.oauth;

import katecam.luvicookie.ditto.domain.member.domain.KakaoUserInfo;
import katecam.luvicookie.ditto.domain.member.domain.Member;
import katecam.luvicookie.ditto.domain.member.domain.PrincipalDetail;
import katecam.luvicookie.ditto.domain.member.domain.Role;
import katecam.luvicookie.ditto.domain.member.repository.MemberRepository;
import katecam.luvicookie.ditto.domain.member.service.PrincipalDetailsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class OAuth2UserCustomService extends DefaultOAuth2UserService {

    private final MemberRepository memberRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User user = super.loadUser(userRequest);
        Map<String, Object> attributes = user.getAttributes();

        KakaoUserInfo kakaoUserInfo = new KakaoUserInfo(attributes);

        String name = kakaoUserInfo.getName();
        String email = kakaoUserInfo.getEmail();
        String profileImage = kakaoUserInfo.getProfileImage();

        Member member = memberRepository.findByEmail(email)
                .orElseGet(() -> saveSocialMember(email, name, profileImage));

        return new PrincipalDetail(member, Collections.singleton(new SimpleGrantedAuthority(member.getRole().getValue())),
                attributes);
    }

    public Member saveSocialMember(String email, String name, String profileImage) {
        Member newMember = Member.builder()
                .email(email)
                .name(name)
                .profileImage(profileImage)
                .role(Role.GUEST)
                .build();
        return memberRepository.save(newMember);
    }
}
