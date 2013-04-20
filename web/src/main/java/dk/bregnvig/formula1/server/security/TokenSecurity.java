package dk.bregnvig.formula1.server.security;

import dk.bregnvig.formula1.Player;
import dk.bregnvig.formula1.client.exception.CredentialException;

public interface TokenSecurity {
	
	String newToken(Player player) throws CredentialException;
	
	Player validate(String token) throws CredentialException;
	
}
