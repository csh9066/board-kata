package boardkata.sever.security;

import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithMockAuthUserSecurityContextFactory.class)
public @interface WithMockAuthUser {

    long id() default 1L;

    String email() default "test@gmail.com";

    String password() default "12345678";
}
