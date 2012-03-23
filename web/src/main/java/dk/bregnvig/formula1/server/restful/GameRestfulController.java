package dk.bregnvig.formula1.server.restful;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import dk.bregnvig.formula1.client.domain.ClientDriver;
import dk.bregnvig.formula1.client.domain.ClientPlayer;
import dk.bregnvig.formula1.client.domain.ClientRace;
import dk.bregnvig.formula1.client.domain.ClientSeason;
import dk.bregnvig.formula1.client.domain.bid.ClientBid;
import dk.bregnvig.formula1.client.domain.wbc.ClientWBCEntry;
import dk.bregnvig.formula1.client.exception.CredentialException;
import dk.bregnvig.formula1.client.service.GameService;

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
	public @ResponseBody void addBid(@RequestBody ClientBid bid) throws CredentialException {
		service.addBid(bid);
	}
	
	@RequestMapping(value="/wbc", method=RequestMethod.GET)
	public @ResponseBody List<ClientWBCEntry> getWbc() throws CredentialException {
		return service.fetchWBCStanding();
	}
	
	@RequestMapping(value="/wbc/{playerName}", method=RequestMethod.GET)
	public @ResponseBody List<ClientWBCEntry> getPlayersWbc(@PathVariable String playerName) throws CredentialException {
		ClientPlayer player = new ClientPlayer();
		player.setPlayername(playerName);
		return service.fetchWBCPlayerEntries(player);
	}
	
	@RequestMapping(value="/season-name", method=RequestMethod.GET)
	public @ResponseBody String getSeasonName() {
		return service.getSeasonName();
	}
	
	@ExceptionHandler(CredentialException.class)
	public void handleCredentialException(CredentialException e, HttpServletResponse response) throws IOException {
		response.sendError(HttpServletResponse.SC_UNAUTHORIZED, e.getMessage());
	}
}
