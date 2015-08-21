package net.woniper.board.config;

import net.woniper.board.domain.User;
import net.woniper.board.domain.type.AuthorityType;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Arrays;
import java.util.Collection;

/**
 * Created by woniper on 15. 1. 29..
 */
public class SecurityUserDetails implements UserDetails {

    private User user;

    public SecurityUserDetails(User user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if(AuthorityType.ADMIN.equals(user.getAuthorityType())) {
            return Arrays.asList(new SimpleGrantedAuthority(AuthorityType.ADMIN.toString()));
        } else {
            return Arrays.asList(new SimpleGrantedAuthority(AuthorityType.USER.toString()));
        }
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
