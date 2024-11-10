package katecam.luvicookie.ditto.domain.login.jwt.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import katecam.luvicookie.ditto.domain.login.jwt.JwtConstants;
import katecam.luvicookie.ditto.domain.login.jwt.TokenProvider;
import katecam.luvicookie.ditto.domain.member.domain.Member;
import katecam.luvicookie.ditto.domain.member.application.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.Objects;

@Slf4j
@RestController
@RequiredArgsConstructor
public class JwtController {

    private final MemberService memberService;

    @GetMapping("/api/auth/reissue")
    public ResponseEntity<String> reissueToken(HttpServletRequest request) {

        // 리프레시 토큰을 쿠키에서 추출
        String refreshToken = extractRefreshToken(request);

        // 리프레시 토큰 유효성 검사
        if (refreshToken == null || !TokenProvider.validateToken(refreshToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid refresh token");
        }

        // 리프레시 토큰에서 사용자 정보 추출
        Integer memberId = Integer.valueOf(Objects.requireNonNull(TokenProvider.getClaims(refreshToken)));
        Member member = memberService.findMemberById(memberId);

        if (member == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid member");
        }

        String newAccessToken = TokenProvider.generateToken(member, JwtConstants.ACCESS_EXP_TIME_MINUTES);
        String newRefreshToken = TokenProvider.generateToken(member, JwtConstants.REFRESH_EXP_TIME_MINUTES);

        // 새 리프레시 토큰을 쿠키에 설정
        ResponseCookie refreshTokenCookie = TokenProvider.createCookie(newRefreshToken);

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString())
                .header(JwtConstants.ACCESS, JwtConstants.JWT_TYPE + newAccessToken)
                .body("Token reissued successfully");
    }

    private String extractRefreshToken(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return null; // 쿠키가 없을 경우 null 반환
        }

        return Arrays.stream(cookies)
                .filter(cookie -> "RefreshToken".equals(cookie.getName()))
                .map(Cookie::getValue)
                .findAny()
                .orElse(null);
    }

}
