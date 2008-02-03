package dk.bregnvig.formula1.server.mapping.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.springframework.beans.BeanUtils;

import dk.bregnvig.formula1.Bid;
import dk.bregnvig.formula1.Driver;
import dk.bregnvig.formula1.Player;
import dk.bregnvig.formula1.Race;
import dk.bregnvig.formula1.Season;
import dk.bregnvig.formula1.account.Account;
import dk.bregnvig.formula1.account.Account.Entry;
import dk.bregnvig.formula1.bid.FastestLapBid;
import dk.bregnvig.formula1.bid.GridBid;
import dk.bregnvig.formula1.bid.PodiumBid;
import dk.bregnvig.formula1.bid.SelectedDriverBid;
import dk.bregnvig.formula1.client.domain.ClientDriver;
import dk.bregnvig.formula1.client.domain.ClientPlayer;
import dk.bregnvig.formula1.client.domain.ClientRace;
import dk.bregnvig.formula1.client.domain.ClientSeason;
import dk.bregnvig.formula1.client.domain.account.ClientAccount;
import dk.bregnvig.formula1.client.domain.account.ClientAccountEntry;
import dk.bregnvig.formula1.client.domain.bid.ClientBid;
import dk.bregnvig.formula1.server.context.WebContext;
import dk.bregnvig.formula1.server.mapping.ObjectFactory;

public class ObjectFactoryImpl implements ObjectFactory{
	
	private WebContext context;

	public ClientRace create(Race race) {
		ClientRace clientRace = new ClientRace(race.isParticipant(context.getPlayer()));
		BeanUtils.copyProperties(race, clientRace, new String[] {"selectedDriver"});
		clientRace.setSelectedDriver(create(race.getSelectedDriver()));
		clientRace.setOpenDate(race.getOpen().getTime());
		for (Bid bid : race.getBids()) {
			clientRace.addBid(create(clientRace.isParticipant(), bid));
		}
		return clientRace;
	}

	public ClientPlayer create(Player player) {
		ClientPlayer clientPlayer = new ClientPlayer();
		BeanUtils.copyProperties(player, clientPlayer);
		return clientPlayer;
	}
	
	public ClientDriver create(Driver driver) {
		ClientDriver clientDriver = new ClientDriver();
		BeanUtils.copyProperties(driver, clientDriver);
		return clientDriver;
	}	
	
	public ClientAccount create(Account account) {
		ClientAccount clientAccount = new ClientAccount();
		clientAccount.setBalance(account.getBalance().intValue());

		List<ClientAccountEntry> entries = new ArrayList<ClientAccountEntry>(account.getEntries().size()); 
		for (Entry entry : account.getEntries()) {
			entries.add(create(entry));
		}
		clientAccount.setEntries(entries);
		return clientAccount;
	}
	
	public ClientSeason create(Season season) {
		ClientSeason clientSeason = new ClientSeason();
		clientSeason.setName(season.getName());
		
		clientSeason.setRaces(getClientRaces(season));
		clientSeason.setPlayers(getClientPlayers(season));
		clientSeason.setDrivers(getClientDrivers(season));
		
		return clientSeason;
	}

	private List<ClientPlayer> getClientPlayers(Season season) {
		Set<Player> players = season.getPlayers();
		
		List<ClientPlayer> clientPlayers = new ArrayList<ClientPlayer>(players.size());
		for (Player player : players) {
			clientPlayers.add(create(player));
		}
		return clientPlayers;
	}

	private List<ClientRace> getClientRaces(Season season) {
		Set<Race> races = season.getRaces();
		
		List<ClientRace> clientRaces = new ArrayList<ClientRace>(races.size());
		for (Race race : races) {
			clientRaces.add(create(race));
		}
		Collections.sort(clientRaces, new ClientRace.OpensComparator());
		return clientRaces;
	}

	private List<ClientDriver> getClientDrivers(Season season) {
		Set<Driver> drivers = season.getDrivers();
		
		List<ClientDriver> clientDrivers = new ArrayList<ClientDriver>(drivers.size());
		for (Driver driver : drivers) {
			clientDrivers.add(create(driver));
		}
		Collections.sort(clientDrivers, new ClientDriver.NumberComparator());
		return clientDrivers;
	}
	
	private ClientBid create(boolean participant, Bid bid) {
		ClientBid clientBid = new ClientBid();
		clientBid.setPlayer(create(bid.getPlayer()));
		if (participant == true) {
			//TODO Add some more copying af properties
		}
		
		return clientBid;
	}
	
	private ClientAccountEntry create(Account.Entry entry) {
		ClientAccountEntry clientEntry = new ClientAccountEntry();
		clientEntry.setAmount(entry.getAmount().intValue());
		clientEntry.setDate(entry.getDate().getTime());
		clientEntry.setMessage(entry.getMessage());
		return clientEntry;
	}

	public void map(ClientPlayer source, Player target) {
		target.setFirstName(source.getFirstName());
		target.setLastName(source.getLastName());
		target.setEmailAddress(source.getEmailAddress());
		target.setSms(source.getSms());
	}
	
	private Driver getDriver(ClientDriver clientDriver) {
		Set<Driver> drivers = context.getSeason().getDrivers();
		for (Driver driver : drivers) {
			if (clientDriver.getId().equals(driver.getId())) {
				return driver;
			}
		}
		throw new IllegalStateException("Client driver does not exist in the core");
	}
	
	public Bid create(ClientDriver[] grid, ClientDriver fastestLap, ClientDriver[] podium, int[] positions, int polePositionTime) {
		Bid bid = new Bid();
		bid.setPlayer(context.getPlayer());
		bid.setGrid(getGridBid(grid));
		bid.setFastestLap(getFastestLapBid(fastestLap));
		bid.setPodium(getPodiumBid(podium));
		bid.setSelectedDriver(getSelectedDriverBid(positions));
		bid.setPolePositionTimeMillis(polePositionTime);
		return bid;
	}
	
	private SelectedDriverBid getSelectedDriverBid(int[] positions) {
		SelectedDriverBid bid = new SelectedDriverBid();
		bid.setStartPosition(positions[0]);
		bid.setEndPosition(positions[1]);
		return bid;
	}

	private PodiumBid getPodiumBid(ClientDriver[] drivers) {
		PodiumBid bid = new PodiumBid();
		bid.setPosition1(getDriver(drivers[0]));
		bid.setPosition2(getDriver(drivers[1]));
		bid.setPosition3(getDriver(drivers[2]));
		return bid;
	}

	private GridBid getGridBid(ClientDriver[] drivers) {
		GridBid grid = new GridBid();
		grid.setPosition1(getDriver(drivers[0]));
		grid.setPosition2(getDriver(drivers[1]));
		grid.setPosition3(getDriver(drivers[2]));
		grid.setPosition4(getDriver(drivers[3]));
		grid.setPosition5(getDriver(drivers[4]));
		grid.setPosition6(getDriver(drivers[5]));
		
		return grid;
	}
	
	private FastestLapBid getFastestLapBid(ClientDriver driver) {
		FastestLapBid bid = new FastestLapBid();
		bid.setDriver(getDriver(driver));
		
		return bid;
	}

	public void setContext(WebContext context) {
		this.context = context;
	}

}
