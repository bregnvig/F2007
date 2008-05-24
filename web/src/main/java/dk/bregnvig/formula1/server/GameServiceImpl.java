package dk.bregnvig.formula1.server;

import java.math.BigDecimal;
import java.util.List;

import org.gwtwidgets.server.spring.ServletUtils;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import dk.bregnvig.formula1.Driver;
import dk.bregnvig.formula1.Player;
import dk.bregnvig.formula1.PlayerRole;
import dk.bregnvig.formula1.Race;
import dk.bregnvig.formula1.Season;
import dk.bregnvig.formula1.client.domain.ClientDriver;
import dk.bregnvig.formula1.client.domain.ClientPlayer;
import dk.bregnvig.formula1.client.domain.ClientRace;
import dk.bregnvig.formula1.client.domain.ClientSeason;
import dk.bregnvig.formula1.client.domain.ClientWBCEntry;
import dk.bregnvig.formula1.client.domain.account.ClientAccount;
import dk.bregnvig.formula1.client.domain.bid.ClientBid;
import dk.bregnvig.formula1.client.domain.bid.ClientResult;
import dk.bregnvig.formula1.client.exception.CredentialException;
import dk.bregnvig.formula1.client.service.GameService;
import dk.bregnvig.formula1.server.context.SessionAttributes;
import dk.bregnvig.formula1.server.mapping.ObjectFactory;
import dk.bregnvig.formula1.server.security.Authorization;

public class GameServiceImpl extends AbstractService implements GameService {

	private static final long serialVersionUID = 3283834885586579712L;

	private ObjectFactory objectFactory;

	private dk.bregnvig.formula1.service.GameService service;

	@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
	public String getSeasonName() {
		return getContext().getSeason().getName();
	}

	@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
	public ClientPlayer login(String playerName, String password) throws CredentialException {
		Player player = getContext().getSeason().getPlayer(playerName);
		player.getRoles(); // Just to load them.
		if (player == null || player.getPassword().equals(password) == false) {
			throw new CredentialException("Player name and or password is correct");
		}
		ServletUtils.getRequest().getSession().setAttribute(SessionAttributes.PLAYER, player);
		return objectFactory.create(player);
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
	public void updatePlayer(ClientPlayer player) {
		objectFactory.map(player, getContext().getPlayer());
		service.updatePlayer(getContext().getPlayer());
	}

	@Authorization(roles = { PlayerRole.PLAYER })
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public void addBid(ClientBid bid) {
		Season season = getContext().getSeason();
		season.getCurrentRace().addBid(objectFactory.create(bid));
	}

	@Authorization(roles = { PlayerRole.PLAYER_ADMIN })
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public ClientRace setResult(ClientResult result) {
		Race race = getContext().getSeason().getCurrentRace();
		race.completeRace(objectFactory.create(result));
		return objectFactory.createFull(race);
	}

	@Authorization(roles = { PlayerRole.PLAYER_ADMIN })
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public void createRace(ClientRace clientRace) {
		Race race = new Race();
		objectFactory.map(clientRace, race);
		getContext().getSeason().addRace(race);
		clientRace.setId(race.getId());
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
		return objectFactory.getClientDrivers(drivers);
	}

	@Authorization(roles = { PlayerRole.ACCOUNT_ADMIN })
	@Transactional(readOnly = true)
	public List<ClientPlayer> findAllPlayers() {
		List<Player> players = service.findAllPlayers();
		return objectFactory.getClientPlayers(players);
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

	public void setObjectFactory(ObjectFactory objectFactory) {
		this.objectFactory = objectFactory;
	}

	public void setService(dk.bregnvig.formula1.service.GameService service) {
		this.service = service;
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
}
