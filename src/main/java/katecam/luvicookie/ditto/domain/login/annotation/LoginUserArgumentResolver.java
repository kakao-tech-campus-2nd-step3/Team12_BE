package katecam.luvicookie.ditto.domain.login.annotation;

import jakarta.servlet.http.HttpServletRequest;
import katecam.luvicookie.ditto.domain.login.jwt.TokenProvider;
import katecam.luvicookie.ditto.domain.member.domain.Member;
import katecam.luvicookie.ditto.domain.member.application.MemberService;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.Objects;

public class LoginUserArgumentResolver implements HandlerMethodArgumentResolver {

    private final MemberService memberService;


    public LoginUserArgumentResolver(MemberService memberService) {
        this.memberService = memberService;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        boolean hasLoginUserAnnotation = parameter.hasParameterAnnotation(LoginUser.class);
        boolean assignableFrom = Member.class.isAssignableFrom(parameter.getParameterType());
        return hasLoginUserAnnotation && assignableFrom;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
        String accessToken = request.getHeader("Authorization");

        if(accessToken == null) return null;

        return memberService.findMemberById(Integer.valueOf(Objects.requireNonNull(TokenProvider.getClaims(TokenProvider.getTokenFromHeader(accessToken)))));
    }
}
