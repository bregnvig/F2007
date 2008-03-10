package dk.bregnvig.formula1.server.mapping.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Set;

import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.beans.BeanUtils;

import dk.bregnvig.formula1.Bid;
import dk.bregnvig.formula1.Driver;
import dk.bregnvig.formula1.Player;
import dk.bregnvig.formula1.PlayerRole;
import dk.bregnvig.formula1.Race;
import dk.bregnvig.formula1.RaceResult;
import dk.bregnvig.formula1.Season;
import dk.bregnvig.formula1.account.Account;
import dk.bregnvig.formula1.account.Account.Entry;
import dk.bregnvig.formula1.account.Account.TransferEntry;
import dk.bregnvig.formula1.bid.AbstractBid;
import dk.bregnvig.formula1.bid.FastestLapBid;
import dk.bregnvig.formula1.bid.FirstCrashBid;
import dk.bregnvig.formula1.bid.FirstCrashResult;
import dk.bregnvig.formula1.bid.GridBid;
import dk.bregnvig.formula1.bid.GridResult;
import dk.bregnvig.formula1.bid.PodiumBid;
import dk.bregnvig.formula1.bid.PodiumResult;
import dk.bregnvig.formula1.bid.SelectedDriverBid;
import dk.bregnvig.formula1.client.domain.ClientDriver;
import dk.bregnvig.formula1.client.domain.ClientPlayer;
import dk.bregnvig.formula1.client.domain.ClientRace;
import dk.bregnvig.formula1.client.domain.ClientSeason;
import dk.bregnvig.formula1.client.domain.account.ClientAccount;
import dk.bregnvig.formula1.client.domain.account.ClientAccountEntry;
import dk.bregnvig.formula1.client.domain.bid.ClientBid;
import dk.bregnvig.formula1.client.domain.bid.ClientResult;
import dk.bregnvig.formula1.server.context.WebContext;
import dk.bregnvig.formula1.server.mapping.ObjectFactory;

public class ObjectFactoryImpl implements ObjectFactory{
	
	private WebContext context;

	public ClientRace create(Race race) {
		ClientRace clientRace = new ClientRace(race.isParticipant(context.getPlayer()));
		BeanUtils.copyProperties(race, clientRace, new String[] {"selectedDriver"});
		clientRace.setSelectedDriver(create(race.getSelectedDriver()));
		clientRace.setOpenDate(race.getOpen().getTime());
		clientRace.setCloseDate(race.getClose().getTime());
		if (race.isCompleted() == false) {
			for (Bid bid : race.getBids()) {
				clientRace.addBid(create(clientRace.isParticipant(), bid, race.isCompleted()));
			}
		} else {
			for (Bid bid : race.getResult()) {
				clientRace.addBid(create(clientRace.isParticipant(), bid, race.isCompleted()));
			}
		}
		
		return clientRace;
	}
	
	public void map(ClientRace source, Race target) {
		target.setName(source.getName());
		Calendar open = new GregorianCalendar();
		open.setTime(source.getOpenDate());
		Calendar close = new GregorianCalendar();
		close.setTime(source.getCloseDate());
				
		target.setOpen(open);
		target.setClose(close);
		
		target.setSelectedDriver(getDriver(source.getSelectedDriver()));
	}
	
	public void map(ClientDriver source, Driver target) {
		BeanUtils.copyProperties(source, target);
	}

	public ClientPlayer create(Player player) {
		ClientPlayer clientPlayer = new ClientPlayer();
		BeanUtils.copyProperties(player, clientPlayer);
		if (player.getRoles().contains(PlayerRole.PLAYER_ADMIN)) {
			clientPlayer.setGameAdministrator(true);
		}
		if (player.getRoles().contains(PlayerRole.ACCOUNT_ADMIN)) {
			clientPlayer.setBankAdministrator(true);
		}
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
			entries.add(create(account, entry));
		}
		clientAccount.setEntries(entries);
		return clientAccount;
	}
	
	public ClientSeason create(Season season) {
		ClientSeason clientSeason = new ClientSeason();
		clientSeason.setName(season.getName());
		
		clientSeason.setRaces(getClientRaces(season));
		clientSeason.setPlayers(getClientPlayers(season));
		clientSeason.setDrivers(getClientDrivers(season.getDrivers()));
		
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

	public List<ClientDriver> getClientDrivers(Collection<Driver> drivers) {
		
		List<ClientDriver> clientDrivers = new ArrayList<ClientDriver>(drivers.size());
		for (Driver driver : drivers) {
			clientDrivers.add(create(driver));
		}
		Collections.sort(clientDrivers, new ClientDriver.NumberComparator());
		return clientDrivers;
	}
	
	private ClientBid create(boolean participant, Bid bid, boolean completed) {
		ClientBid clientBid = new ClientBid();
		clientBid.setPlayer(create(bid.getPlayer()));
		if (participant == true || completed == true) {
			clientBid.setGrid(getDrivers(bid.getGrid(), "position", 6));
			clientBid.setFastestLap(create(bid.getFastestLap().getDriver()));
			clientBid.setPodium(getDrivers(bid.getPodium(), "position", 3));
			clientBid.setSelectedDriver(new int[] {bid.getSelectedDriver().getStartPosition(), bid.getSelectedDriver().getEndPosition()});
			clientBid.setFirstCrash(create(bid.getFirstCrash().getCrash1()));
			clientBid.setPolePositionTime(bid.getPolePositionTimeMillis());
			if (completed == true) {
				clientBid.setPoints(bid.getPoints());
			}
		}
		return clientBid;
	}
	
	private ClientDriver[] getDrivers(AbstractBid bid, String propertyName, int numberOfProperties) {
		ClientDriver[] drivers = new ClientDriver[numberOfProperties];
		for (int i = 0; i < numberOfProperties; i++) {
			try {
				drivers[i] = create((Driver) PropertyUtils.getSimpleProperty(bid, propertyName+(i+1)));
				
			} catch (Exception e) {
				throw new IllegalArgumentException("The supplied bid could not have property " + propertyName+(i+1) + " extracted", e);
			}
		}
		return drivers;
	}
	
	private ClientAccountEntry create(Account account, Account.Entry entry) {
		ClientAccountEntry clientEntry = new ClientAccountEntry();
		clientEntry.setAmount(entry.getAmount().intValue());
		if (entry instanceof Account.TransferEntry) {
			TransferEntry transferEntry = (TransferEntry) entry;
			if (transferEntry.getFromAccount().equals(account)) {
				clientEntry.setAmount(-clientEntry.getAmount());
			} 
		}
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
	
	public Bid create(ClientBid clientBid) {
		Bid bid = new Bid();
		bid.setPlayer(context.getSeason().getPlayer(context.getPlayer().getPlayername()));
		bid.setGrid(getGridBid(clientBid.getGrid()));
		bid.setFastestLap(getFastestLapBid(clientBid.getFastestLap()));
		bid.setPodium(getPodiumBid(clientBid.getPodium()));
		bid.setSelectedDriver(getSelectedDriverBid(clientBid.getSelectedDriver()));
		bid.setFirstCrash(getFirstCrashBid(clientBid.getFirstCrash()));
		bid.setPolePositionTimeMillis(clientBid.getPolePositionTime());
		return bid;
	}
	
	public RaceResult create(ClientResult clientResult) {
		RaceResult result = new RaceResult();
		result.setPlayer(context.getPlayer());
		result.setGrid(getGridResult(clientResult.getGrid()));
		result.setFastestLap(getFastestLapBid(clientResult.getFastestLap()));
		result.setPodium(getPodiumResult(clientResult.getPodium()));
		result.setSelectedDriver(getSelectedDriverBid(clientResult.getSelectedDriver()));
		result.setFirstCrash(getFirstCrashResult(clientResult.getFirstCrashes()));
		result.setPolePositionTimeMillis(clientResult.getPolePositionTime());
		return result;
	}
	
	private SelectedDriverBid getSelectedDriverBid(int[] positions) {
		SelectedDriverBid bid = new SelectedDriverBid();
		bid.setStartPosition(positions[0]);
		bid.setEndPosition(positions[1]);
		return bid;
	}

	private PodiumBid getPodiumBid(ClientDriver[] drivers) {
		PodiumBid bid = new PodiumBid();
		return mapPodiumBid(drivers, bid);
	}

	private PodiumResult getPodiumResult(ClientDriver[] drivers) {
		PodiumResult result = new PodiumResult();
		mapPodiumBid(drivers, result);
		result.setPosition4(getDriver(drivers[3]));
		return result;
	}
	
	private PodiumBid mapPodiumBid(ClientDriver[] drivers, PodiumBid bid) {
		bid.setPosition1(getDriver(drivers[0]));
		bid.setPosition2(getDriver(drivers[1]));
		bid.setPosition3(getDriver(drivers[2]));
		return bid;
	}

	private GridBid getGridBid(ClientDriver[] drivers) {
		return mapGridBid(drivers, new GridBid());
	}
	
	private GridResult getGridResult(ClientDriver[] drivers) {
		GridResult result = new GridResult();
		mapGridBid(drivers, result);
		result.setPosition7(getDriver(drivers[6]));
		return result;
	}

	private GridBid mapGridBid(ClientDriver[] drivers, GridBid grid) {
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

	private FirstCrashBid getFirstCrashBid(ClientDriver driver) {
		FirstCrashBid bid = new FirstCrashBid();
		return mapFirstCrashBid(driver, bid);
	}
	
	private FirstCrashResult getFirstCrashResult(ClientDriver[] drivers) {
		FirstCrashResult result = new FirstCrashResult();
		mapFirstCrashBid(drivers[0], result);
		result.setCrash2(getDriver(drivers[1]));
		result.setCrash3(getDriver(drivers[2]));
		return result;
	}

	private FirstCrashBid mapFirstCrashBid(ClientDriver driver, FirstCrashBid bid) {
		bid.setCrash1(getDriver(driver));
		return bid;
	}
	
	public void setContext(WebContext context) {
		this.context = context;
	}

}
