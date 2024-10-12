package katecam.luvicookie.ditto.domain.member.domain;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class PrincipalDetail implements OAuth2User, UserDetails {
    private Member member;
    private Collection<? extends GrantedAuthority> authorities;

    private Map<String, Object> attributes;
    public PrincipalDetail(Member member, Collection<? extends GrantedAuthority> authorities){
        this.member = member;
        this.authorities = authorities;

    }
    public PrincipalDetail(Member member, Collection<? extends GrantedAuthority> authorities, Map<String, Object> attributes){
        this.member = member;
        this.authorities = authorities;
        this.attributes = attributes;
    }

    public Map<String, Object> getMemberInfo() {
        Map<String, Object> info = new HashMap<>();
        info.put("name", member.getName());
        //info.put("email", user.getEmail());
        info.put("role", member.getRole());
        return info;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return null;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return member.getName();
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }

    @Override
    public String getName() {
        return null;
    }

    public Integer getId() {
        return member.getId();
    }

    public Member getUser() {
        return member;
    }
}
