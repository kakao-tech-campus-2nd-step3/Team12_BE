package katecam.luvicookie.ditto.domain.login.config;


import katecam.luvicookie.ditto.domain.login.annotation.LoginUserArgumentResolver;
import katecam.luvicookie.ditto.domain.member.application.MemberService;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class Config implements WebMvcConfigurer {
    private final MemberService memberService;

    public Config(MemberService memberService) {
        this.memberService = memberService;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new LoginUserArgumentResolver(memberService));
    }
}
