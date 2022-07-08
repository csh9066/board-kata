package boardkata.sever.securituy;

import lombok.Builder;
import lombok.NonNull;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class UserPrincipal implements UserDetails {

    private final Long id;

    private final String email;

    private final String password;

    private final List<GrantedAuthority> authorities;

    @Builder
    public UserPrincipal(@NonNull Long id,
                         @NonNull String email,
                         @NonNull String password) {
        this.id = id;
        this.email = email;
        this.password = password;
        // 일단은 하드코딩 (추후 ADMIN 인가 필요할 시 추가 구현)
        this.authorities = Arrays.asList(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    public Long getId() {
        return this.id;
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
