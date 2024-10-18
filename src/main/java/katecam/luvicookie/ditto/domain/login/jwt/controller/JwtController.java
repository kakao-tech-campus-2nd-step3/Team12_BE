package katecam.luvicookie.ditto.domain.login.jwt.controller;

import katecam.luvicookie.ditto.domain.login.TokenDTO;
import katecam.luvicookie.ditto.domain.login.jwt.JwtConstants;
import katecam.luvicookie.ditto.domain.login.jwt.TokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
public class JwtController {

    @GetMapping("/api/auth/reissue")
    public Map<String, Object> refresh(@RequestHeader("Authorization") String authHeader, @RequestBody TokenDTO tokenDTO) {
        log.info("Refresh Token = {}", tokenDTO.getToken());
        String refreshToken = tokenDTO.getToken();
        if (authHeader == null) {
            throw new RuntimeException("Access Token 이 존재하지 않습니다");
        } else if (!authHeader.startsWith(JwtConstants.JWT_TYPE)) {
            throw new RuntimeException("BEARER 로 시작하지 않는 올바르지 않은 토큰 형식입니다");
        }

        String accessToken = TokenProvider.getTokenFromHeader(authHeader);

        // Access Token 의 만료 여부 확인
        if (!TokenProvider.isExpired(accessToken)) {
            return Map.of("Access Token", accessToken, "Refresh Token", refreshToken);
        }

        // refreshToken 검증 후 새로운 토큰 생성 후 전달
        Map<String, Object> claims = TokenProvider.validateToken(refreshToken);
        String newAccessToken = TokenProvider.generateToken(claims, JwtConstants.ACCESS_EXP_TIME);

        String newRefreshToken = refreshToken;
        long expTime = TokenProvider.tokenRemainTime((Integer) claims.get("exp"));   // Refresh Token 남은 만료 시간
        log.info("Refresh Token Remain Expire Time = {}", expTime);
        // Refresh Token 의 만료 시간이 한 시간도 남지 않은 경우
        if (expTime <= 60) {
            newRefreshToken = TokenProvider.generateToken(claims, JwtConstants.REFRESH_EXP_TIME);
        }

        return Map.of("accessToken", newAccessToken, "refreshToken", newRefreshToken);
    }
}
