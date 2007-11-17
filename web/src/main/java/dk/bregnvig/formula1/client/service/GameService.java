package dk.bregnvig.formula1.client.service;

import com.google.gwt.user.client.rpc.RemoteService;

import dk.bregnvig.formula1.client.exception.CredentialException;

/**
 * This interface contains all the operation that are season related. 
 * @author 4u603ctpsz9
 *
 */
public interface GameService extends RemoteService {

	/**
	 * Returns the current season. The current season is defined in the web.xml
	 * @return
	 */
	String getSeasonName(); 

	/**
	 * Tries to log the specified user into the system
	 * @param playerName
	 * @param password
	 */
	void login(String playerName, String password) throws CredentialException;
}
