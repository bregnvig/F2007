package dk.bregnvig.formula1.server.context;

import dk.bregnvig.formula1.Player;
import dk.bregnvig.formula1.Season;

/**
 * Usermethods exposes the context the F2007 application has
 * @author 4u603ctpsz9
 *
 */
public interface WebContext {
	
	/**
	 * Returns the current 
	 * @return
	 */
	Season getSeason();
	
	/**
	 * Sets the current player
	 * @param user
	 */
	void setPlayer(Player player);
	
	/**
	 * Gets the current player. Null if no player is logged in
	 * @return
	 */
	Player getPlayer();
	
	/**
	 * Remove any thread local variables
	 */
	void remove();
}
