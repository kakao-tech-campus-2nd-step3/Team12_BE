package katecam.luvicookie.ditto.domain.login.oauth;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import katecam.luvicookie.ditto.domain.login.jwt.JwtConstants;
import katecam.luvicookie.ditto.domain.login.jwt.TokenProvider;
import katecam.luvicookie.ditto.domain.member.domain.Member;
import katecam.luvicookie.ditto.domain.member.domain.PrincipalDetail;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;


@RequiredArgsConstructor
public class LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {

        PrincipalDetail principal = (PrincipalDetail) authentication.getPrincipal();
        Member member = principal.getUser();
        Map<String, Object> responseMap = principal.getMemberInfo();

        String accessToken = TokenProvider.generateToken(responseMap, JwtConstants.ACCESS_EXP_TIME);
        String refreshToken = TokenProvider.generateToken(responseMap, JwtConstants.REFRESH_EXP_TIME);

        // 최초 로그인인 경우 추가 정보 입력을 위한 회원가입 페이지로 리다이렉트
        if (member.isGuest()) {

            response.addHeader(JwtConstants.ACCESS, JwtConstants.JWT_TYPE + accessToken);
            response.addHeader(JwtConstants.REFRESH, JwtConstants.JWT_TYPE + refreshToken);
            String redirectURL = UriComponentsBuilder.fromUriString("http://localhost:8080/user/info")
                    //.queryParam("email", member.getEmail()).build()
                    .encode(StandardCharsets.UTF_8)
                    .toUriString();
            getRedirectStrategy().sendRedirect(request, response, redirectURL);

        } else {

            response.addHeader(JwtConstants.ACCESS, JwtConstants.JWT_TYPE + accessToken);
            response.addHeader(JwtConstants.REFRESH, JwtConstants.JWT_TYPE + refreshToken);

            // 최초 로그인이 아닌 경우 로그인 성공 페이지로 이동
            String redirectURL = UriComponentsBuilder.fromUriString("http://localhost:8080/interest/public")
                    .build()
                    .encode(StandardCharsets.UTF_8)
                    .toUriString();
            getRedirectStrategy().sendRedirect(request, response, redirectURL);
        }
    }
}
