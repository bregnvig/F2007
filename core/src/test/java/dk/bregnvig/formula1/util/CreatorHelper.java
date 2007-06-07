package dk.bregnvig.formula1.util;

import java.util.Calendar;

import dk.bregnvig.formula1.model.Player;
import dk.bregnvig.formula1.model.Race;

public class CreatorHelper {

	public Player getPlayer(String playername) {
		Player player = new Player();
		player.setPlayername(playername);
		player.setFirstName("first"+playername);
		player.setLastName("last"+playername);
		player.setEmail("bob@bob.dk");
		player.setSms("12345678");
		
		return player;
	}
	
	public Race getRace(String name) {
		Race race = new Race();
		race.setName(name);
		race.setOpen(Calendar.getInstance());
		Calendar close = Calendar.getInstance();
		close.add(Calendar.WEEK_OF_YEAR, 1);
		race.setClose(close);
		return race;
	}
}
