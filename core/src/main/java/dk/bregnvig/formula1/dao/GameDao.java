package dk.bregnvig.formula1.dao;

import java.util.List;

import dk.bregnvig.formula1.Driver;
import dk.bregnvig.formula1.Player;
import dk.bregnvig.formula1.Season;

public interface GameDao extends CommonDao {

	Season findByName(String name);
	
	Player findPlayerByName(String playerName);
	
	void updatePlayer(Player player);
	
	List<Driver> findAllDrivers();
	
	List<Player> findAllPlayers();
}
