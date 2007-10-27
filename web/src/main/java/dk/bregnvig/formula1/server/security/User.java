package dk.bregnvig.formula1.server.security;

public interface User {
	
	boolean accessToRoles(UserRole[] roles);

}
