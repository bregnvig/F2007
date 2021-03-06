package dk.bregnvig.formula1.server.mapping;

import java.util.Collection;
import java.util.List;
import java.util.Set;

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
import dk.bregnvig.formula1.client.domain.wbc.ClientHistory;
import dk.bregnvig.formula1.client.domain.wbc.ClientWBC;
import dk.bregnvig.formula1.client.domain.wbc.ClientWBCEntry;
import dk.bregnvig.formula1.wbc.WBC;
import dk.bregnvig.formula1.wbc.WBC.History;

public interface ObjectFactory {
	
	ClientSeason create(Season season);
	
	ClientWBC create(WBC wbc);

	ClientPlayer create(Player player);
	
	ClientRace create(Race source);
	
	ClientHistory create(History history);
	
	ClientRace createFull(Race source);
	
	ClientBid create(Race race, Bid bid);
	
	ClientAccount create(Account account);
	
	Set<ClientDriver> getClientDrivers(Collection<Driver> drivers);

	List<ClientPlayer> getClientPlayers(Collection<Player> players);
	
	Bid create(ClientBid clientBid);
	
	List<ClientWBCEntry> create(List<WBC.Entry> entries);
	
	RaceResult create(ClientResult clientResult);

	void map(ClientPlayer source, Player target);
	
	void map(ClientRace source, Race target);
	
	void map(ClientDriver source, Driver target);
}
