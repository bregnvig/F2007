package dk.bregnvig.formula1.client;

import com.google.gwt.user.client.rpc.RemoteService;

/**
 * This interface contains all the operation that are season related. 
 * @author 4u603ctpsz9
 *
 */
public interface SeasonService extends RemoteService {

	/**
	 * Returns the current season. The current season is defined in the web.xml
	 * @return
	 */
	String getSeasonName(); 

}
