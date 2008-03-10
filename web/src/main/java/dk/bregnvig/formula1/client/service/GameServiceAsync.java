package dk.bregnvig.formula1.client.service;

import com.google.gwt.user.client.rpc.AsyncCallback;

import dk.bregnvig.formula1.client.domain.ClientDriver;
import dk.bregnvig.formula1.client.domain.ClientPlayer;
import dk.bregnvig.formula1.client.domain.ClientRace;
import dk.bregnvig.formula1.client.domain.bid.ClientBid;
import dk.bregnvig.formula1.client.domain.bid.ClientResult;

/**
 * {@link GameService}
 * @author 4u603ctpsz9
 */
public interface GameServiceAsync {

	void getSeasonName(AsyncCallback callback); 

	void login(String playerName, String password, AsyncCallback callback);
	
	void getSeason(AsyncCallback callback);
	
	void getCurrentRace(AsyncCallback callback);

	void getAccount(AsyncCallback callback);

	void updatePassword(String password, AsyncCallback callback);

	void updatePlayer(ClientPlayer player, AsyncCallback callback);

	void createPlayer(ClientPlayer player, AsyncCallback callback);
	
	void addBid(ClientBid bid, AsyncCallback callback);
	
	void setResult(ClientResult result, AsyncCallback callback);

	void createRace(ClientRace race, AsyncCallback callback);

	void updateRace(ClientRace race, AsyncCallback callback);

	void createDriver(ClientDriver driver, AsyncCallback callback);

	void updateDriver(ClientDriver driver, AsyncCallback callback);
	
	void findAllDrivers(AsyncCallback callback);
}
