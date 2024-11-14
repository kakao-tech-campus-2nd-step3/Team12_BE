package katecam.luvicookie.ditto.domain.login;

import katecam.luvicookie.ditto.domain.login.jwt.TokenProvider;
import katecam.luvicookie.ditto.domain.login.oauth.LoginFailHandler;
import katecam.luvicookie.ditto.domain.login.oauth.LoginSuccessHandler;
import katecam.luvicookie.ditto.domain.login.oauth.OAuth2UserCustomService;
import katecam.luvicookie.ditto.domain.member.application.PrincipalDetailsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
@Slf4j
public class SecurityConfig {

    private final OAuth2UserCustomService oAuth2UserService;
    private final TokenProvider tokenProvider;
    private final PrincipalDetailsService principalDetailsService;
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {  //해당 URL은 필터 거치지 않겠다
        return (web -> web.ignoring().requestMatchers("/img/**", "/css/**", "/js/**", "/h2-console/**"));
    }
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();

        corsConfiguration.setAllowedOriginPatterns(List.of("*"));
        corsConfiguration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "HEAD", "OPTIONS"));
        corsConfiguration.setAllowedHeaders(List.of("Authorization", "Cache-Control", "Content-Type"));
        corsConfiguration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration); // 모든 경로에 대해서 CORS 설정을 적용

        return source;
    }


    @Bean
    public LoginSuccessHandler LoginSuccessHandler() {
        return new LoginSuccessHandler();
    }


    @Bean
    public LoginFailHandler LoginFailHandler() {
        return new LoginFailHandler();
    }


    @Bean
    public TokenAuthenticationFilter TokenAuthenticationFilter(TokenProvider tokenProvider) {
        return new TokenAuthenticationFilter(tokenProvider);
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(principalDetailsService);
        return authProvider;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.cors(httpSecurityCorsConfigurer -> httpSecurityCorsConfigurer.configurationSource(corsConfigurationSource()));

        http.csrf(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .logout(AbstractHttpConfigurer::disable);


        http.sessionManagement(httpSecuritySessionManagementConfigurer -> {
            httpSecuritySessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.NEVER);
        });

        http.authorizeHttpRequests(authorizationManagerRequestMatcherRegistry ->
                authorizationManagerRequestMatcherRegistry.anyRequest().permitAll());

        http.addFilterBefore(TokenAuthenticationFilter(tokenProvider), UsernamePasswordAuthenticationFilter.class);


        http.oauth2Login(httpSecurityOAuth2LoginConfigurer ->
                httpSecurityOAuth2LoginConfigurer
                        .authorizationEndpoint(end -> end.baseUri("/api/oauth2/authorization/"))
                        .redirectionEndpoint((endPoint) -> endPoint
                                .baseUri("/api/login/oauth2/code/kakao"))
                        .loginPage("/user/login/kakao")
                        .successHandler(LoginSuccessHandler())
                        .failureHandler(LoginFailHandler())
                        .userInfoEndpoint(userInfoEndpointConfig ->
                                userInfoEndpointConfig.userService(oAuth2UserService)));

        return http.build();
    }
}
