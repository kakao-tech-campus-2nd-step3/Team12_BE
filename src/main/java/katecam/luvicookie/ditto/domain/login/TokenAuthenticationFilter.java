package katecam.luvicookie.ditto.domain.login;

import com.nimbusds.jose.shaded.gson.Gson;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import katecam.luvicookie.ditto.domain.login.jwt.JwtConstants;
import katecam.luvicookie.ditto.domain.login.jwt.TokenProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.PatternMatchUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

@Slf4j
public class TokenAuthenticationFilter extends OncePerRequestFilter {

    private final TokenProvider tokenProvider;
    private static final String[] whitelist = {"/api/studies", "/api/auth", "/api/auth/kakao", "/user/login/kakao", "/api/auth/reissue"};

    public TokenAuthenticationFilter(TokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    private static void checkAuthorizationHeader(String header) {
        if(header == null) {
            throw new RuntimeException("토큰이 전달되지 않았습니다");
        } else if (!header.startsWith(JwtConstants.JWT_TYPE)) {
            throw new RuntimeException("BEARER 로 시작하지 않는 올바르지 않은 토큰 형식입니다");
        }
    }
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String requestURI = request.getRequestURI();
        return PatternMatchUtils.simpleMatch(whitelist, requestURI);
    }
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String authHeader = request.getHeader(JwtConstants.JWT_HEADER);


        try {
            checkAuthorizationHeader(authHeader);   // header 가 올바른 형식인지 체크
            String token = TokenProvider.getTokenFromHeader(authHeader);
            Authentication authentication = tokenProvider.getAuthentication(token);

            log.info("authentication = {}", authentication);

            SecurityContextHolder.getContext().setAuthentication(authentication);

            filterChain.doFilter(request, response);    // 다음 필터로 이동
        } catch (Exception e) {
            Gson gson = new Gson();
            String json = "";
            if (e instanceof ExpiredJwtException) {
                json = gson.toJson(Map.of("Token_Expired", e.getMessage()));
            } else {
                json = gson.toJson(Map.of("error", e.getMessage()));
            }

            response.setContentType("application/json; charset=UTF-8");
            PrintWriter printWriter = response.getWriter();
            printWriter.println(json);
            printWriter.close();
        }
    }

}
