package dk.bregnvig.formula1.server.security;

import java.util.StringTokenizer;

import org.apache.commons.codec.binary.Base64;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import dk.bregnvig.formula1.Player;
import dk.bregnvig.formula1.client.exception.CredentialException;
import dk.bregnvig.formula1.server.context.WebContext;

public class TokenSecurityImpl implements TokenSecurity {
	
	private static String SECRET_KEY = "156111a948f04b5aacadff7de8479782";
	
	private WebContext context;

	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public Player validate(String token) throws CredentialException {
		StringTokenizer tokenizer = new StringTokenizer(new String(new Base64().decode(token.getBytes())), ":");
		if (tokenizer.countTokens() != 2) throw new CredentialException("The specified token does the required format. Token: " + new Base64().decode(token.getBytes()));
		Player player = context.getSeason().getPlayer(tokenizer.nextToken());
		if (player == null) throw new CredentialException("Token not valid");
		if (SimpleMD5.MD5(player.getPassword() + SECRET_KEY).equals(tokenizer.nextToken().trim()) == false) throw new CredentialException("Token not valid");
		player.getRoles();
		return player; 
	}

	@Override
	public String newToken(Player player) {
		return SimpleMD5.MD5(player.getPassword() + SECRET_KEY);
	}

	public void setContext(WebContext context) {
		this.context = context;
	}

}
