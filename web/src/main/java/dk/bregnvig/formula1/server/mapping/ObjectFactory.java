package dk.bregnvig.formula1.server.mapping;

import dk.bregnvig.formula1.Player;
import dk.bregnvig.formula1.Race;
import dk.bregnvig.formula1.Season;
import dk.bregnvig.formula1.account.Account;
import dk.bregnvig.formula1.client.domain.ClientPlayer;
import dk.bregnvig.formula1.client.domain.ClientRace;
import dk.bregnvig.formula1.client.domain.ClientSeason;
import dk.bregnvig.formula1.client.domain.account.ClientAccount;

public interface ObjectFactory {
	
	ClientSeason create(Season season);

	ClientPlayer create(Player player);
	
	ClientRace create(Race source);
	
	ClientAccount create(Account account);
	
	void map(ClientPlayer source, Player target);
}
