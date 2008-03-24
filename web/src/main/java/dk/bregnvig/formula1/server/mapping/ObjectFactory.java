package dk.bregnvig.formula1.server.mapping;

import java.util.Collection;
import java.util.List;

import dk.bregnvig.formula1.Bid;
import dk.bregnvig.formula1.Driver;
import dk.bregnvig.formula1.Player;
import dk.bregnvig.formula1.Race;
import dk.bregnvig.formula1.RaceResult;
import dk.bregnvig.formula1.Season;
import dk.bregnvig.formula1.account.Account;
import dk.bregnvig.formula1.client.domain.ClientDriver;
import dk.bregnvig.formula1.client.domain.ClientPlayer;
import dk.bregnvig.formula1.client.domain.ClientRace;
import dk.bregnvig.formula1.client.domain.ClientSeason;
import dk.bregnvig.formula1.client.domain.account.ClientAccount;
import dk.bregnvig.formula1.client.domain.bid.ClientBid;
import dk.bregnvig.formula1.client.domain.bid.ClientResult;

public interface ObjectFactory {
	
	ClientSeason create(Season season);

	ClientPlayer create(Player player);
	
	ClientRace create(Race source);
	
	ClientRace createFull(Race source);
	
	ClientAccount create(Account account);
	
	List<ClientDriver> getClientDrivers(Collection<Driver> drivers);
	
	Bid create(ClientBid clientBid);
	
	RaceResult create(ClientResult clientResult);

	void map(ClientPlayer source, Player target);
	
	void map(ClientRace source, Race target);
	
	void map(ClientDriver source, Driver target);
}
