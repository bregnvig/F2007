package dk.bregnvig.formula1.client.service;

import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;

import dk.bregnvig.formula1.client.domain.ClientDriver;
import dk.bregnvig.formula1.client.domain.ClientPlayer;
import dk.bregnvig.formula1.client.domain.ClientRace;
import dk.bregnvig.formula1.client.domain.ClientSeason;
import dk.bregnvig.formula1.client.domain.ClientWBCEntry;
import dk.bregnvig.formula1.client.domain.account.ClientAccount;
import dk.bregnvig.formula1.client.domain.bid.ClientBid;
import dk.bregnvig.formula1.client.domain.bid.ClientResult;
import dk.bregnvig.formula1.client.exception.CredentialException;

/**
 * This interface contains all the operation that are season related. 
 * @author 4u603ctpsz9
 *
 */
public interface GameService extends RemoteService {

	
	/**
	 * Returns the name of the current season. The current season is defined in the web.xml
	 * @return
	 */
	String getSeasonName(); 

	/**
	 * Returns the the current season. The current season is defined in the web.xml
	 * @return
	 */
	ClientSeason getSeason(); 

	/**
	 * Tries to log the specified user into the system
	 * @param playerName
	 * @param password
	 */
	ClientPlayer login(String playerName, String password) throws CredentialException;
	
	/**
	 * Returns the current race
	 * @return
	 */
	ClientRace getCurrentRace();

	/**
	 * Returns the race specified by the id. This is only because of performance :-(
	 * @return
	 */
	ClientRace getRace(Long id);

	/**
	 * Returns the user that is currently logged in account
	 * @return
	 */
	ClientAccount getAccount();
	
	/**
	 * Returns the specifed players account
	 * @return
	 */
	ClientAccount getAccount(ClientPlayer player);
	
	/**
	 * Updates the players password
	 * @param password
	 */
	void updatePassword(String password);
	
	/**
	 * Updates this player information
	 * @param player
	 */
	void updatePlayer(ClientPlayer player);

	/**
	 * Updates this player information
	 * @param player
	 */
	void createPlayer(ClientPlayer player);
	
	/**
	 * Adds this bid to the open race
	 * @param bid
	 */
	void addBid(ClientBid bid);
	
	/**
	 * Sets the result for the closed race
	 * @param result
	 */
	void setResult(ClientRace clientRace, ClientResult result);
	
	/**
	 * Sets the URL that the result will be fetched by
	 * @param url
	 */
	void setAutoResult(ClientRace clientRace, String url);
	
	/**
	 * Rolls back the result of the race
	 */
	void rollbackRace(ClientRace race);
	
	/**
	 * Adds a race to the current season
	 * @param race
	 */
	ClientRace createRace(ClientRace race);

	/**
	 * Updates a race
	 * @param race
	 */
	void updateRace(ClientRace race);

	/**
	 * Adds a driver to the current season
	 * @param race
	 */
	void createDriver(ClientDriver driver);

	/**
	 * Updates a driver
	 * @param race
	 */
	void updateDriver(ClientDriver driver);
	
	void accountTransfer(ClientPlayer fromPlayer, ClientPlayer toPlayer, String message, int amount);
	void accountDeposit(ClientPlayer player, String message, int amount);
	void accountWithdraw(ClientPlayer player, String message, int amount);

	/**
	 * Returns the list of all drivers in the system.
	 * @return
	 */
	List<ClientDriver> findAllDrivers();

	/**
	 * Returns the list of all players in the system.
	 * @return
	 */
	List<ClientPlayer> findAllPlayers();
	
	List<ClientWBCEntry> fetchWBCStanding();

	
	List<ClientWBCEntry> fetchWBCStanding(ClientRace race);
}
