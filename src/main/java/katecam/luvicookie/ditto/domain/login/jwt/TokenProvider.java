package katecam.luvicookie.ditto.domain.login.jwt;


import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import katecam.luvicookie.ditto.domain.member.domain.Member;
import katecam.luvicookie.ditto.domain.member.domain.PrincipalDetail;
import katecam.luvicookie.ditto.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.Map;

@Component
@Slf4j
@Configuration
@RequiredArgsConstructor
public class TokenProvider {
    public static String secretKey = JwtConstants.key;
    private final MemberService memberService;


    public static String getTokenFromHeader(String header) {
        return header.split(" ")[1];
    }

    public static String generateToken(Member member, int validTime) {
        SecretKey key = null;
        try {
            key = Keys.hmacShaKeyFor(TokenProvider.secretKey.getBytes(StandardCharsets.UTF_8));
        } catch(Exception e){
            throw new RuntimeException(e.getMessage());
        }
        return Jwts.builder()
                .setHeader(Map.of("typ","JWT"))
                .setSubject(member.getId().toString())
                .claim("email", member.getEmail())
                .setIssuedAt(Date.from(ZonedDateTime.now().toInstant()))
                .setExpiration(Date.from(ZonedDateTime.now().plusMinutes(validTime).toInstant()))
                .signWith(key)
                .compact();
    }


    public Authentication getAuthentication(String token) {
        String claims = getClaims(token);
        Member member = memberService.findMemberById(Integer.valueOf(claims));

        PrincipalDetail principalDetail = new PrincipalDetail(member);
        return new UsernamePasswordAuthenticationToken(principalDetail, "", principalDetail.getAuthorities());

    }

    public static Map<String, Object> validateToken(String token) {
        Map<String, Object> claim = null;

        SecretKey key = Keys.hmacShaKeyFor(TokenProvider.secretKey.getBytes(StandardCharsets.UTF_8));
        claim = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token) // 파싱 및 검증, 실패 시 에러
                .getBody();

        return claim;
    }

    public static boolean isExpired(String token) {
        try {
            validateToken(token);
        } catch (Exception e) {
            return (e instanceof ExpiredJwtException);
        }
        return false;
    }

    public static long tokenRemainTime(Integer expTime) {
        Date expDate = new Date((long) expTime * (1000));
        long remainMs = expDate.getTime() - System.currentTimeMillis();
        return remainMs / (1000 * 60);
    }

    public static String getClaims(String jwt) {
        String tokenFromHeader = getTokenFromHeader(jwt);
        try{
            return Jwts.parserBuilder()
                    .setSigningKey(Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8)))
                    .build()
                    .parseClaimsJws(tokenFromHeader)
                    .getBody()
                    .getSubject();

        }
        catch (JwtException e){
            return null;
        }
    }

    public static ResponseCookie createCookie(String refreshToken) { // 수정
        String cookieName = "RefreshToken";

        ResponseCookie cookie = ResponseCookie.from(cookieName, refreshToken)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(Duration.ofDays(1))
                .sameSite("Strict")
                .build();

        return cookie;
    }

}
