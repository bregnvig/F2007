package dk.bregnvig.formula1.server.security;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Authorization {
	
	/**
	 * Defines the set of roles that are allowed to access this service
	 * @return an array of roles
	 */
	UserRole[] roles() default {};
}
