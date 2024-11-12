package katecam.luvicookie.ditto.domain.member.domain;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

@Slf4j
public class KakaoUserInfo {
    @Getter
    public static String socialId;
    public static Map<String, Object> account;
    public static Map<String, Object> profile;

    public KakaoUserInfo(Map<String , Object> attributes) {
        socialId = String.valueOf(attributes.get("id"));
        account = (Map<String, Object>) attributes.get("kakao_account");
        profile = (Map<String, Object>) account.get("profile");
        log.info(account.toString());
    }

    public String getEmail(){return String.valueOf(account.get("email"));}
    public String getName(){
        return String.valueOf(profile.get("nickname"));
    }
    public String getProfileImage(){
        return String.valueOf(profile.get("profile_image_url"));
    }
}
