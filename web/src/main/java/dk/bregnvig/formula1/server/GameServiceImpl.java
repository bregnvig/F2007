package dk.bregnvig.formula1.server;

import org.gwtwidgets.server.spring.ServletUtils;

import dk.bregnvig.formula1.Player;
import dk.bregnvig.formula1.client.exception.CredentialException;
import dk.bregnvig.formula1.client.service.GameService;
import dk.bregnvig.formula1.server.context.SessionAttributes;


public class GameServiceImpl extends AbstractService implements GameService {

	private static final long serialVersionUID = 3283834885586579712L;

	public String getSeasonName() {
		return getContext().getSeason().getName();
	}

	public void login(String playerName, String password) throws CredentialException {
		Player player = getContext().getSeason().getPlayer(playerName);
		if (player == null || player.getPassword().equals(password) == false) {
			throw new CredentialException("Player name and or password is correct");
		}
		ServletUtils.getRequest().getSession().setAttribute(SessionAttributes.PLAYER, player);
	}

}
