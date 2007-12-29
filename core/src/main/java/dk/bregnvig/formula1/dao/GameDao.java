package dk.bregnvig.formula1.dao;

import dk.bregnvig.formula1.Player;
import dk.bregnvig.formula1.Season;

public interface GameDao {

	Season findByName(String name);
	
	Player findPlayerByName(String playerName);
	
	void updatePlayer(Player player);
}
