package dk.bregnvig.formula1.server;

import org.gwtwidgets.server.spring.ServletUtils;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import dk.bregnvig.formula1.Player;
import dk.bregnvig.formula1.PlayerRole;
import dk.bregnvig.formula1.Race;
import dk.bregnvig.formula1.Season;
import dk.bregnvig.formula1.client.domain.ClientPlayer;
import dk.bregnvig.formula1.client.domain.ClientRace;
import dk.bregnvig.formula1.client.domain.ClientSeason;
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
		if (player == null || player.getPassword().equals(password) == false) {
			throw new CredentialException("Player name and or password is correct");
		}
		ServletUtils.getRequest().getSession().setAttribute(SessionAttributes.PLAYER, player);
		return objectFactory.create(player);
	}

	@Authorization(roles={PlayerRole.PLAYER})
	@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
	public ClientRace getCurrentRace() {
		Race race = getContext().getSeason().getCurrentRace();
		if (race != null) {
			return objectFactory.create(race);
		}
		return null;
	}

	@Authorization(roles={PlayerRole.PLAYER})
	@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
	public ClientSeason getSeason() {
		return objectFactory.create(getContext().getSeason());
	}
	
	@Authorization(roles = {PlayerRole.PLAYER})
	@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
	public ClientAccount getAccount() {
		return objectFactory.create(getContext().getSeason().getPlayer(getContext().getPlayer().getPlayername()).getAccount());
	}

	@Authorization(roles = {PlayerRole.PLAYER})
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public void updatePassword(String password) {
		getContext().getPlayer().setPassword(password);
		service.updatePlayer(getContext().getPlayer());
	}
	
	@Authorization(roles = {PlayerRole.PLAYER})
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public void updatePlayer(ClientPlayer player) {
		objectFactory.map(player, getContext().getPlayer());
		service.updatePlayer(getContext().getPlayer());
	}

	@Authorization(roles = {PlayerRole.PLAYER})
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public void addBid(ClientBid bid) {
		Season season = getContext().getSeason(); 
		season.getCurrentRace().addBid(objectFactory.create(bid));
	}

	@Authorization(roles = {PlayerRole.PLAYER_ADMIN})
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public ClientRace setResult(ClientResult result) {
		Race race = getContext().getSeason().getCurrentRace(); 
		race.completeRace(objectFactory.create(result));
		return objectFactory.create(race);
	}

	public void setObjectFactory(ObjectFactory objectFactory) {
		this.objectFactory = objectFactory;
	}

	public void setService(dk.bregnvig.formula1.service.GameService service) {
		this.service = service;
	}

}
