package katecam.luvicookie.ditto.domain.login.jwt;


import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import katecam.luvicookie.ditto.domain.member.domain.PrincipalDetail;
import katecam.luvicookie.ditto.domain.member.domain.Role;
import katecam.luvicookie.ditto.domain.member.domain.Member;
import katecam.luvicookie.ditto.domain.member.service.PrincipalDetailsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.Date;
import java.util.Map;
import java.util.Set;

@Component
@Slf4j
@Configuration
@RequiredArgsConstructor
public class TokenProvider {
    public static String secretKey = JwtConstants.key;
    private final PrincipalDetailsService principalDetailsService;


    public static String getTokenFromHeader(String header) {
        return header.split(" ")[1];
    }

    public static String generateToken(Map<String, Object> valueMap, int validTime) {
        SecretKey key = null;
        try {
            key = Keys.hmacShaKeyFor(TokenProvider.secretKey.getBytes(StandardCharsets.UTF_8));
        } catch(Exception e){
            throw new RuntimeException(e.getMessage());
        }
        return Jwts.builder()
                .setHeader(Map.of("typ","JWT"))
                .setClaims(valueMap)
                .setIssuedAt(Date.from(ZonedDateTime.now().toInstant()))
                .setExpiration(Date.from(ZonedDateTime.now().plusMinutes(validTime).toInstant()))
                .signWith(key)
                .compact();
    }

    public Authentication getAuthentication(String token) {
        Map<String, Object> claims = validateToken(token);

        String name = (String) claims.get("name");

        UserDetails userDetails = principalDetailsService.loadUserByUsername(name);

        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());

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

}
