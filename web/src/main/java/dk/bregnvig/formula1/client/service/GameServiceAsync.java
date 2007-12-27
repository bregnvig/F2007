package dk.bregnvig.formula1.client.service;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * {@link GameService}
 * @author 4u603ctpsz9
 */
public interface GameServiceAsync {

	void getSeasonName(AsyncCallback callback); 

	void login(String playerName, String password, AsyncCallback callback);
	
	void getSeason(AsyncCallback callback);
	
	void getOpenRace(AsyncCallback callback);

	void getAccount(AsyncCallback callback);

}
