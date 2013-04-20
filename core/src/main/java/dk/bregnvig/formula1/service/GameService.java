package dk.bregnvig.formula1.service;

import java.util.List;

import dk.bregnvig.formula1.Driver;
import dk.bregnvig.formula1.Player;
import dk.bregnvig.formula1.Season;

/**
 * Provides services not are not accessiable via the rich domain model
 * @author 4u603ctpsz9
 */
public interface GameService {

	/**
	 * Returns the season given by the name. Null if no season was found
	 * @param name
	 * @return
	 */
	Season findByName(String name);
	
	/**
	 * Updates this player. This is accross seasons.
	 * @param player
	 */
	void updatePlayer(Player player);
	
	/**
	 * Creates the specified player. The created player will no be part of any seasons
	 * @param player
	 */
	void createPlayer(Player player);
	
	/**
	 * Returns a list of all drivers. No mather the season
	 * @return
	 */
	List<Driver> findAllDrivers();

	/**
	 * Creates a driver. Not attached to any season
	 * @param driver
	 */
	void createDriver(Driver driver);
	
	/**
	 * Updates this player. This is accross seasons.
	 * @param player
	 */
	void updateDriver(Driver driver);

	/**
	 * Returns a list of all players. No mather the season
	 * @return
	 */
	List<Player> findAllPlayers();
}
