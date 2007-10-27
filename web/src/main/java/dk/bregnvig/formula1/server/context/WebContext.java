package dk.bregnvig.formula1.server.context;

import dk.bregnvig.formula1.Season;
import dk.bregnvig.formula1.server.security.User;

/**
 * These methods exposes the context the F2007 application has
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
	 * Sets the current user
	 * @param user
	 */
	void setUser(User user);
	
	/**
	 * Gets the current user. Null if no user is logged in
	 * @return
	 */
	User getUser();
	
	/**
	 * Remove any thread local variables
	 */
	void remove();
}
