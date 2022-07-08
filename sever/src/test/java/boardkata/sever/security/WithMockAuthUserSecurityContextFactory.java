package boardkata.sever.security;

import boardkata.sever.securituy.UserPrincipal;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

public class WithMockAuthUserSecurityContextFactory
        implements WithSecurityContextFactory<WithMockAuthUser> {

    @Override
    public SecurityContext createSecurityContext(WithMockAuthUser authUser) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();

        UserPrincipal principal =
                new UserPrincipal(authUser.id(), authUser.email(), authUser.password());

        Authentication auth =
                UsernamePasswordAuthenticationToken.authenticated(principal,
                        principal.getPassword(),
                        principal.getAuthorities());

        context.setAuthentication(auth);
        return context;
    }
}
