package dk.bregnvig.formula1.server;

import org.gwtwidgets.server.spring.ServletUtils;

import dk.bregnvig.formula1.Player;
import dk.bregnvig.formula1.PlayerRole;
import dk.bregnvig.formula1.Race;
import dk.bregnvig.formula1.client.domain.ClientPlayer;
import dk.bregnvig.formula1.client.domain.ClientRace;
import dk.bregnvig.formula1.client.domain.ClientSeason;
import dk.bregnvig.formula1.client.domain.account.ClientAccount;
import dk.bregnvig.formula1.client.exception.CredentialException;
import dk.bregnvig.formula1.client.service.GameService;
import dk.bregnvig.formula1.server.context.SessionAttributes;
import dk.bregnvig.formula1.server.mapping.ObjectFactory;
import dk.bregnvig.formula1.server.security.Authorization;


public class GameServiceImpl extends AbstractService implements GameService {

	private static final long serialVersionUID = 3283834885586579712L;
	
	private ObjectFactory objectFactory;

	public String getSeasonName() {
		return getContext().getSeason().getName();
	}

	public ClientPlayer login(String playerName, String password) throws CredentialException {
		Player player = getContext().getSeason().getPlayer(playerName);
		if (player == null || player.getPassword().equals(password) == false) {
			throw new CredentialException("Player name and or password is correct");
		}
		ServletUtils.getRequest().getSession().setAttribute(SessionAttributes.PLAYER, player);
		return objectFactory.create(player);
	}

	@Authorization(roles={PlayerRole.PLAYER})
	public ClientRace getOpenRace() {
		Race race = getContext().getSeason().getOpenRace();
		if (race != null) {
			return objectFactory.create(race);
		}
		return null;
	}

	@Authorization(roles={PlayerRole.PLAYER})
	public ClientSeason getSeason() {
		return objectFactory.create(getContext().getSeason());
	}
	
	@Authorization(roles = {PlayerRole.PLAYER})
	public ClientAccount getAccount() {
		return objectFactory.create(getContext().getPlayer().getAccount());
	}


	public void setObjectFactory(ObjectFactory objectFactory) {
		this.objectFactory = objectFactory;
	}
}
