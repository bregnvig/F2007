package dk.bregnvig.formula1.server;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import dk.bregnvig.formula1.Bid;
import dk.bregnvig.formula1.Driver;
import dk.bregnvig.formula1.Player;
import dk.bregnvig.formula1.PlayerRole;
import dk.bregnvig.formula1.Race;
import dk.bregnvig.formula1.RaceResult;
import dk.bregnvig.formula1.Season;
import dk.bregnvig.formula1.bid.ResultStrategy;
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
import dk.bregnvig.formula1.client.exception.CredentialException;
import dk.bregnvig.formula1.client.service.GameService;
import dk.bregnvig.formula1.scraping.AutomaticResult;
import dk.bregnvig.formula1.server.context.SessionAttributes;
import dk.bregnvig.formula1.server.mapping.ObjectFactory;
import dk.bregnvig.formula1.server.security.Authorization;
import dk.bregnvig.formula1.server.security.TokenSecurity;
import dk.bregnvig.formula1.wbc.WBC.History;

public class GameServiceImpl extends AbstractService implements GameService {

	private ObjectFactory objectFactory;

	private dk.bregnvig.formula1.service.GameService service;
	private	ResultStrategy resultStrategy;
	private AutomaticResult automaticResult;
	private TokenSecurity tokenSecurity;

	@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
	public String getSeasonName() {
		return getContext().getSeason().getName();
	}
	
	@Override
	@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
	public ClientWBC getWBC() {
		return objectFactory.create(getContext().getSeason().getWBC());
	}

	@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
	public ClientPlayer login(String playerName, String password) throws CredentialException {
		Player player = getContext().getSeason().getPlayer(playerName);
		player.getRoles(); // Just to load them.
		if (player == null || player.getPassword().equals(password) == false) {
			throw new CredentialException("Player name and or password is incorrect");
		}
		ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		requestAttributes.getRequest().getSession().setAttribute(SessionAttributes.PLAYER, player);
		ClientPlayer clientPlayer =  objectFactory.create(player);
		clientPlayer.setToken(tokenSecurity.newToken(player));
		return clientPlayer;
	}

	@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
	public ClientRace getCurrentRace() {
		Race race = getContext().getSeason().getCurrentRace();
		if (race != null) {
			return objectFactory.create(race);
		}
		return null;
	}

	@Authorization(roles = { PlayerRole.PLAYER })
	@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
	public ClientRace getRace(Long id) {
		Race race = getContext().getSeason().getRaceById(id);
		if (race != null) {
			return objectFactory.createFull(race);
		}
		return null;
	}

	@Authorization(roles = { PlayerRole.PLAYER })
	@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
	public ClientSeason getSeason() {
		ClientSeason season = objectFactory.create(getContext().getSeason());
		return season;
	}

	@Authorization(roles = { PlayerRole.PLAYER })
	@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
	public ClientAccount getAccount() {
		return objectFactory.create(getContext().getSeason().getPlayer(getContext().getPlayer().getPlayername()).getAccount());
	}

	@Authorization(roles = { PlayerRole.PLAYER_ADMIN })
	@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
	public ClientAccount getAccount(ClientPlayer player) {
		return objectFactory.create(getContext().getSeason().getPlayer(player.getPlayername()).getAccount());
	}

	@Authorization(roles = { PlayerRole.PLAYER })
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public void updatePassword(String password) {
		getContext().getPlayer().setPassword(password);
		service.updatePlayer(getContext().getPlayer());
	}

	@Authorization(roles = { PlayerRole.PLAYER_ADMIN })
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public void createPlayer(ClientPlayer clientPlayer) {
		Player player = new Player(PlayerRole.PLAYER);
		objectFactory.map(clientPlayer, player);
		player.setPlayername(clientPlayer.getPlayername());
		player.setPassword("skalændres");
		service.createPlayer(player);
		getContext().getSeason().addPlayer(player);
	}

	@Authorization(roles = { PlayerRole.PLAYER })
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public void updatePlayer(ClientPlayer clientPlayer) {
		Season season = getContext().getSeason();
		Player player = getContext().getPlayer().isPlayerInRole(PlayerRole.PLAYER_ADMIN) ? season.getPlayer(clientPlayer.getPlayername()) : getContext().getPlayer();
		objectFactory.map(clientPlayer, player);
		
		service.updatePlayer(player);
		if (clientPlayer.isPartOfSeason() == true) {
			season.addPlayer(player);
		} else {
			season.removePlayer(player);
		}
	}

	@Authorization(roles = { PlayerRole.PLAYER })
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public void addBid(ClientBid bid) {
		Season season = getContext().getSeason();
		season.getCurrentRace().addBid(objectFactory.create(bid));
	}

	@Override
	@Authorization(roles = { PlayerRole.PLAYER })
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public void joinWBC() {
		Season season = getContext().getSeason();
		Player player = season.getPlayer(getContext().getPlayer().getPlayername());
		season.getWBC().joinWBC(player);
	}
	
	@Authorization(roles = { PlayerRole.PLAYER_ADMIN })
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public void setResult(ClientRace clientRace, ClientResult result) {
		Race race = getContext().getSeason().getRaceById(clientRace.getId());
		race.completeRace(objectFactory.create(result));
	}

	@Override
	@Authorization(roles = { PlayerRole.PLAYER_ADMIN })
	@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
	public ClientRace getIntermediateResult(ClientRace clientRace, ClientResult clientRaceResult) {
		Race race = getContext().getSeason().getRaceById(clientRace.getId());
		Set<Bid> bids = race.getBids();
		RaceResult raceResult = objectFactory.create(clientRaceResult);
		clientRace.getBids().clear();
		resultStrategy.calculateResult(raceResult, bids);
		for (Bid bid : bids) {
			clientRace.addBid(objectFactory.create(race, bid));
		}
		Collections.sort(clientRace.getBids(), new Comparator<ClientBid>() {

			@Override
			public int compare(ClientBid o1, ClientBid o2) {
				if (o1.getPoints() < o2.getPoints()) {
					return 1;
				}
				if (o1.getPoints() > o2.getPoints()) {
					return -1;
				}
				return 0;
			}
		});

		return clientRace;
	}
	
	@Authorization(roles = { PlayerRole.PLAYER_ADMIN })
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public void setAutoResult(ClientRace clientRace, String url) {
		Race race = getContext().getSeason().getRaceById(clientRace.getId());
		automaticResult.setURL(url);
		RaceResult result = automaticResult.getRaceResult(race);
		result.setPlayer(getContext().getPlayer());
		race.completeRace(result);
	}

	@Authorization(roles = { PlayerRole.PLAYER_ADMIN })
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public ClientRace createRace(ClientRace clientRace) {
		Race race = new Race();
		objectFactory.map(clientRace, race);
		getContext().getSeason().addRace(race);
		clientRace.setId(race.getId());
		return clientRace;
	}

	@Authorization(roles = { PlayerRole.PLAYER_ADMIN })
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public void rollbackRace(ClientRace clientRace) {
		Season season = getContext().getSeason();
		Race race = season.getRaceById(clientRace.getId()); 
		race.rollbackRace();
		season.getWBC().raceRolledBack(race);
	}

	@Authorization(roles = { PlayerRole.PLAYER_ADMIN })
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public void updateRace(ClientRace clientRace) {
		Race race = getContext().getSeason().getRaceById(clientRace.getId());
		objectFactory.map(clientRace, race);
	}

	@Authorization(roles = { PlayerRole.PLAYER_ADMIN })
	@Transactional(readOnly = true)
	public List<ClientDriver> findAllDrivers() {
		List<Driver> drivers = service.findAllDrivers();
		ClientSeason clientSeason = objectFactory.create(getContext().getSeason());
		List<ClientDriver> clientDrivers = new ArrayList<ClientDriver>(objectFactory.getClientDrivers(drivers));
		for (ClientDriver clientDriver : clientDrivers) {
			clientDriver.setPartOfSeason(clientSeason.getDrivers().contains(clientDriver));
		}
		Collections.sort(clientDrivers, new ClientDriver.NumberComparator());
		return clientDrivers;
	}

	@Authorization(roles = { PlayerRole.ACCOUNT_ADMIN })
	@Transactional(readOnly = true)
	public List<ClientPlayer> findAllPlayers() {
		List<Player> players = service.findAllPlayers();
		ClientSeason clientSeason = objectFactory.create(getContext().getSeason());
		List<ClientPlayer> clientPlayers = objectFactory.getClientPlayers(players);
		for (ClientPlayer clientPlayer : clientPlayers) {
			clientPlayer.setPartOfSeason(clientSeason.getPlayers().contains(clientPlayer));
		}
		return clientPlayers;
	}
	
	@Authorization(roles = { PlayerRole.PLAYER_ADMIN })
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public void createDriver(ClientDriver clientDriver) {
		Driver driver = new Driver();
		objectFactory.map(clientDriver, driver);
		service.createDriver(driver);
		getContext().getSeason().addDriver(driver);
	}

	@Authorization(roles = { PlayerRole.PLAYER_ADMIN })
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public void updateDriver(ClientDriver clientDriver) {
		Driver driver = new Driver();
		objectFactory.map(clientDriver, driver);
		service.updateDriver(driver);
		if (clientDriver.isPartOfSeason()) {
			getContext().getSeason().addDriver(driver);
		} else {
			getContext().getSeason().removeDriver(driver);
		}
	}

	@Authorization(roles = { PlayerRole.PLAYER })
	@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
	public List<ClientWBCEntry> fetchWBCStanding() {
		return objectFactory.create(getContext().getSeason().getWBC().getStanding());
	}

	@Authorization(roles = { PlayerRole.PLAYER })
	@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
	public List<ClientWBCEntry> fetchWBCStanding(ClientRace clientRace) {
		Season season = getContext().getSeason();
		Race race = season.getRaceById(clientRace.getId());
		return objectFactory.create(getContext().getSeason().getWBC().getRaceEntries(race));
	}
	
	@Authorization(roles = { PlayerRole.PLAYER })
	@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
	public List<ClientWBCEntry> fetchWBCPlayerEntries(ClientPlayer clientPlayer) {
		Season season = getContext().getSeason();
		Player player = season.getPlayer(clientPlayer.getPlayername());
		return objectFactory.create(season.getWBC().getPlayerEntries(player));
	}

	public void setObjectFactory(ObjectFactory objectFactory) {
		this.objectFactory = objectFactory;
	}

	public void setService(dk.bregnvig.formula1.service.GameService service) {
		this.service = service;
	}
	
	public void setResultStrategy(ResultStrategy strategy) {
		this.resultStrategy = strategy;
	}

	@Authorization(roles = { PlayerRole.ACCOUNT_ADMIN })
	@Transactional(readOnly = false)
	public void accountDeposit(ClientPlayer clientPlayer, String message, int amount) {
		Player player = getContext().getSeason().getPlayer(clientPlayer.getPlayername());
		player.getAccount().deposit(message, new BigDecimal(amount));
	}

	@Authorization(roles = { PlayerRole.ACCOUNT_ADMIN })
	@Transactional(readOnly = false)
	public void accountTransfer(ClientPlayer clientFromPlayer, ClientPlayer clientToPlayer, String message, int amount) {
		Player fromPlayer = getContext().getSeason().getPlayer(clientFromPlayer.getPlayername());
		Player toPlayer = getContext().getSeason().getPlayer(clientToPlayer.getPlayername());
		fromPlayer.getAccount().transfer(message, new BigDecimal(amount), toPlayer.getAccount());
	}

	@Authorization(roles = { PlayerRole.ACCOUNT_ADMIN })
	@Transactional(readOnly = false)
	public void accountWithdraw(ClientPlayer clientPlayer, String message, int amount) {
		Player player = getContext().getSeason().getPlayer(clientPlayer.getPlayername());
		player.getAccount().withdraw(message, new BigDecimal(amount));
	}

	@Required
	public void setAutomaticResult(AutomaticResult automaticResult) {
		this.automaticResult = automaticResult;
	}

	@Authorization(roles = { PlayerRole.PLAYER })
	@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
	public List<ClientHistory> getHistory() {
		List<History> histories = getContext().getSeason().getWBC().getHistory();
		List<ClientHistory> result = new ArrayList<ClientHistory>(histories.size());
		for (History history : histories) {
			result.add(objectFactory.create(history));
		}
		return result;
	}

	public void setTokenSecurity(TokenSecurity tokenSecurity) {
		this.tokenSecurity = tokenSecurity;
	}
	
	
}
