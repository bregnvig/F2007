package dk.bregnvig.formula1.server.restful;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import dk.bregnvig.formula1.account.NotEnoughMoneyException;
import dk.bregnvig.formula1.client.domain.ClientDriver;
import dk.bregnvig.formula1.client.domain.ClientPassword;
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
import dk.bregnvig.formula1.wbc.WBCException;

@Controller
public class GameRestfulController {
	
	@Autowired private GameService service;
 	private Log log = LogFactory.getLog(GameRestfulController.class);
 	
 	public GameRestfulController() {
 		log.info("GameRestController has started");
 	}
 	
	@RequestMapping(value="/login/{playerName}/{password}", method=RequestMethod.GET)
	public @ResponseBody ClientPlayer login(@PathVariable String playerName, @PathVariable String password) throws CredentialException {
		ClientPlayer player = service.login(playerName, password);
		return player;
	}
	
	@RequestMapping(value="/season", method=RequestMethod.GET)
	public @ResponseBody ClientSeason season() throws CredentialException {
		return service.getSeason();
	}
	
	@RequestMapping(value="/race", method=RequestMethod.GET, params="!players")
	public @ResponseBody ClientRace currentRace() throws CredentialException {
		return service.getCurrentRace();
	}
	
	@RequestMapping(value="/race/{id}", method=RequestMethod.GET)
	public @ResponseBody ClientRace getRace(@PathVariable Long id) throws CredentialException {
		return service.getRace(id);
	}
	
	@RequestMapping(value="/races", method=RequestMethod.GET)
	public @ResponseBody List<ClientRace> getRaces() throws CredentialException {
		return service.getSeason().getRaces();
	}
	
	@RequestMapping(value="/race/{id}/bid/{player}", method=RequestMethod.GET)
	public @ResponseBody ClientBid getBid(@PathVariable Long id, @PathVariable String player) throws CredentialException {
		ClientRace race = service.getRace(id);
		if (race != null) {
			for (ClientBid bid : race.getBids()) {
				if (bid.getPlayer().getPlayername().equals(player)) return bid;
			}
		}
		return null;
	}
	
	@RequestMapping(value="/race/{id}", method=RequestMethod.GET, params="players")
	public @ResponseBody List<ClientBid> getPlayers(@PathVariable Long id) throws CredentialException {
		ClientRace race = service.getRace(id);
		if (race != null) {
			return race.getBids();
		}
		return null;
	}
	
	@RequestMapping(value="/race/{id}", method=RequestMethod.POST)
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public @ResponseBody void setRaceResult(@PathVariable Long id, @RequestBody ClientResult result) throws CredentialException {
		ClientRace race = service.getRace(id);
		if (race != null) {
			service.setResult(race, result);
		}
	}
	
	@RequestMapping(value="/race/{id}", method=RequestMethod.DELETE)
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public @ResponseBody void rollback(@PathVariable Long id) throws CredentialException {
		ClientRace race = service.getRace(id);
		if (race != null) {
			service.rollbackRace(race);
		}
	}
	
	@RequestMapping(value="/race", method=RequestMethod.GET, params="players")
	public @ResponseBody List<ClientPlayer> getPlayers() throws CredentialException {
		
		ClientRace race = service.getCurrentRace();
		race = service.getRace(race.getId());
		
		List<ClientPlayer> players = new ArrayList<ClientPlayer>();
		for (ClientBid bid : race.getBids()) {
			players.add(bid.getPlayer());
		}
		return players;
	}
	
	@RequestMapping(value="/race/drivers", method=RequestMethod.GET)
	public @ResponseBody List<ClientDriver> activeDrivers() throws CredentialException {
		ClientSeason season  = service.getSeason();
		return season.getActiveDriver();
	}
	
	@RequestMapping(value="/bid", method=RequestMethod.POST)
	public @ResponseBody void addBid(@RequestBody ClientBid bid, HttpServletResponse response) throws CredentialException, IOException {
		try {
			service.addBid(bid);
		} catch (NotEnoughMoneyException e) {
			response.sendError(HttpServletResponse.SC_PAYMENT_REQUIRED, e.getMessage());
		}
	}
	
	@RequestMapping(value={"/wbc", "v2/wbc/players"}, method=RequestMethod.GET, params="!graph")
	public @ResponseBody List<ClientWBCEntry> getWbcPlayers() throws CredentialException {
		return service.fetchWBCStanding();
	}
	
	@RequestMapping(value="v2/wbc", method=RequestMethod.GET)
	public @ResponseBody ClientWBC getWbc() throws CredentialException {
		return service.getWBC();
	}	
	
	@RequestMapping(value={"/wbc/{playerName}", "v2/wbc/players/{playerName}"}, method=RequestMethod.GET, params="!graph")
	public @ResponseBody List<ClientWBCEntry> getPlayerWbc(@PathVariable String playerName) throws CredentialException {
		ClientPlayer player = new ClientPlayer();
		player.setPlayername(playerName);
		return service.fetchWBCPlayerEntries(player);
	}
	
	@RequestMapping(value={"/wbc", "v2/wbc/players"}, method=RequestMethod.GET, params="graph")
	public @ResponseBody List<ClientHistory> getWbcPlayersGraph() throws CredentialException {
		return service.getHistory();
	}
	
	@RequestMapping(value="/player/account", method=RequestMethod.GET)
	public @ResponseBody ClientAccount getAccount() throws CredentialException {
		return service.getAccount();
	}
	
	@RequestMapping(value="/player/{playerName}", method=RequestMethod.PUT)
	public @ResponseBody void updatePlayer(@PathVariable String playerName, @RequestBody ClientPlayer player, HttpServletResponse response) throws CredentialException, IOException {
		try {
			service.updatePlayer(player);
		} catch (NotEnoughMoneyException e) {
			response.sendError(HttpServletResponse.SC_PAYMENT_REQUIRED, e.getMessage());
		}
	}
	
	@RequestMapping(value="/player/{playerName}/password", method=RequestMethod.PUT)
	public @ResponseBody void updatePassword(@PathVariable String playerName, @RequestBody ClientPassword password, HttpServletResponse response) throws CredentialException, IOException {
		try {
			service.updatePassword(password.getPassword());
		} catch (NotEnoughMoneyException e) {
			response.sendError(HttpServletResponse.SC_PAYMENT_REQUIRED, e.getMessage());
		}
	}
	
	@RequestMapping(value={"/player/wbc", "v2/wbc/players/{playerName}"}, method=RequestMethod.POST)
	public @ResponseBody void joinWBC(@PathVariable String playerName, HttpServletResponse response) throws CredentialException, IOException {
		try {
			service.joinWBC();
		} catch (NotEnoughMoneyException e) {
			response.sendError(HttpServletResponse.SC_PAYMENT_REQUIRED, e.getMessage());
		} catch (WBCException e) {			
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
		}
	}

	@RequestMapping(value="/season-name", method=RequestMethod.GET, produces="text/plain; charset=utf-8")
	public @ResponseBody String getSeasonName() {
		return service.getSeasonName();
	}
	
	@ExceptionHandler(CredentialException.class)
	public void handleCredentialException(CredentialException e, HttpServletResponse response) throws IOException {
		response.sendError(HttpServletResponse.SC_UNAUTHORIZED, e.getMessage());
	}
}
