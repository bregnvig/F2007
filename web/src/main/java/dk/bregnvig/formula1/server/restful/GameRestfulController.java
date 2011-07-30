package dk.bregnvig.formula1.server.restful;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import dk.bregnvig.formula1.client.domain.ClientPlayer;
import dk.bregnvig.formula1.client.domain.ClientRace;
import dk.bregnvig.formula1.client.domain.ClientSeason;
import dk.bregnvig.formula1.client.exception.CredentialException;
import dk.bregnvig.formula1.client.service.GameService;
import dk.bregnvig.formula1.server.security.TokenSecurity;

@Controller
public class GameRestfulController {
	
	@Autowired private GameService service;
 	@Autowired private TokenSecurity tokenSecurity; 
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
	
	@RequestMapping(value="/current-race", method=RequestMethod.GET)
	public @ResponseBody ClientRace currentRace() throws CredentialException {
		return service.getCurrentRace();
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
