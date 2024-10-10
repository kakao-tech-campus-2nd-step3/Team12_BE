package katecam.luvicookie.ditto.domain.login.oauth;

import katecam.luvicookie.ditto.domain.user.domain.KakaoUserInfo;
import katecam.luvicookie.ditto.domain.user.domain.PrincipalDetail;
import katecam.luvicookie.ditto.domain.user.domain.Role;
import katecam.luvicookie.ditto.domain.user.domain.User;
import katecam.luvicookie.ditto.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
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

    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User user = super.loadUser(userRequest);
        Map<String, Object> attributes = user.getAttributes();

        KakaoUserInfo kakaoUserInfo = new KakaoUserInfo(attributes);
        String socialId = KakaoUserInfo.getSocialId();
        log.info(socialId);
        String name = kakaoUserInfo.getName();

        User member = userRepository.findBySocialId(socialId)
                .orElseGet(() -> saveSocialMember(socialId, name));

        return new PrincipalDetail(member, Collections.singleton(new SimpleGrantedAuthority(member.getRole().getValue())),
                attributes);
    }

    public User saveSocialMember(String socialId, String name) {
        User newMember = User.builder()
                    .socialId(socialId)
                    .name(name)
                    .role(Role.GUEST)
                    .build();
        return userRepository.save(newMember);
    }
}
