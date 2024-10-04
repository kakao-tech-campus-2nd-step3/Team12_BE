package katecam.luvicookie.ditto.domain.user.domain;

import katecam.luvicookie.ditto.domain.user.domain.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class PrincipalDetail implements OAuth2User, UserDetails {
    private User user;
    private Collection<? extends GrantedAuthority> authorities;

    private Map<String, Object> attributes;
    public PrincipalDetail(User user, Collection<? extends GrantedAuthority> authorities){
        this.user = user;
        this.authorities = authorities;

    }
    public PrincipalDetail(User user, Collection<? extends GrantedAuthority> authorities, Map<String, Object> attributes){
        this.user = user;
        this.authorities = authorities;
        this.attributes = attributes;
    }

    public Map<String, Object> getMemberInfo() {
        Map<String, Object> info = new HashMap<>();
        info.put("name", user.getName());
        //info.put("email", user.getEmail());
        info.put("role", user.getRole());
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
        return user.getName();
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

    public Long getId() {
        return user.getId();
    }

    public User getUser() {
        return user;
    }
}
